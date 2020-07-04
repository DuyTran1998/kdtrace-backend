package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.ProducerMapper;
import com.duytran.kdtrace.mapper.ProductMapper;
import com.duytran.kdtrace.model.*;
import com.duytran.kdtrace.repository.ProducerRepository;
import com.duytran.kdtrace.repository.ProductRepositoty;
import com.duytran.kdtrace.repository.QRCodeRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.IntStream;

@Service
public class ProductService {
    @Autowired
    ProductRepositoty productRepository;

    @Autowired
    QRCodeRepository qrCodeRepository;

    @Autowired
    UserPrincipalService userPrincipalService;

    @Autowired
    ProducerService producerService;

    @Autowired
    CommonService commonService;

    @Autowired
    BlockchainService blockchainService;

    @Autowired
    ProducerRepository producerRepository;

    @Transactional
    public ResponseModel createProduct(ProductModel productModel) {
        Product product = ProductMapper.INSTANCE.productModelToProduct(productModel);
        Producer producer = producerService.getProducerInPrincipal();
        product.setProducer(producer);
        product.setCreate_at(commonService.getDateTime());
        productRepository.save(product);
        List<QRCode> qrCodes = generateCode(product);
        qrCodeRepository.saveAll(qrCodes);
        product.setCodes(qrCodes);
        productRepository.save(product);
        blockchainService.updateProduct(producer.getUser(), product.getId(), "kdtrace");
        blockchainService.createQRCodes(producer.getUser(), product.getId(), "kdtrace");
        return new ResponseModel("Create Successfully", HttpStatus.OK.value(), productModel);
    }

    @Value("${url}")
    private String url;

    private List<QRCode> generateCode(Product product) {
        List<QRCode> qrCodes = new ArrayList<>();
        IntStream.rangeClosed(1, (int) product.getQuantity()).forEach(
                i -> {
                    String code = null;
                    try {
                        code = URLEncoder.encode("P" + product.getId() + "-N" + i, StandardCharsets.UTF_8.toString());
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String link = url + code;
                    qrCodes.add(new QRCode(product, code, userPrincipalService.getUserCurrentLogined(), link,
                            StatusQRCode.AVAILABLE, commonService.getDateTime()));
                }
        );
        return qrCodes;
    }

    @Transactional
    public ResponseModel getAllProducts() {
        Long producerID = producerService.getProducerInPrincipal().getId();
        List<Product> products = productRepository.findAllByProducer_Id(producerID);
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProductToModel(products);
        return new ResponseModel("List Product", HttpStatus.OK.value(), productModels);
    }

    @Transactional
    public boolean checkQuanlityProducts(Long id_product, long quanlity) {
        long quanlity_products = productRepository.getQuanlityProducts(id_product);
        return (quanlity_products >= quanlity);
    }

    @Transactional
    public Product getProductEntityById(Long id) {
        return productRepository.findProductById(id).orElseThrow(
                () -> new RecordNotFoundException("Product is not exist")
        );
    }

    public ResponseModel getProductById(Long id) {
        Product product = getProductEntityById(id);
        ProductModel productModel = ProductMapper.INSTANCE.productToProductModel(product);
        return new ResponseModel("Successfully", HttpStatus.OK.value(), productModel);
    }

    @Transactional
    public void changeStatusQRCode(User user, Long productID, long quanlity, StatusQRCode statusQRCode) {
        Product product = productRepository.findProductById(productID).get();
        List<QRCode> qrCodes;
        if (statusQRCode == StatusQRCode.WAITING) {
            product.setQuantity(product.getQuantity() - quanlity);
            qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(productID, "AVAILABLE");
        } else {
            product.setQuantity(product.getQuantity() + quanlity);
            qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(productID, "WAITING");
        }

        List<Long> qrCodeIds = new ArrayList<>();
        IntStream.rangeClosed(0, (int) quanlity - 1).forEach(
                i -> {
                    QRCode qrCode = qrCodes.get(i);
                    qrCode.setStatusQRCode(statusQRCode);
                    qrCodeIds.add(qrCode.getId());
                    qrCodeRepository.save(qrCode);
                });
        blockchainService.saveQRCodes(user, qrCodeIds, statusQRCode, null, "Org1", "kdtrace");
        productRepository.save(product);
    }

    public boolean checkExistProductByIdAndProducer(Long id, Long producer_id) {
        return productRepository.existsByIdAndProducer_Id(id, producer_id);
    }

    public Long trackingCode(String code, String otp) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElse(new QRCode());
        if (otp.equals(qrCode.getOtp())) {
            if (qrCode.getTracking() == null)
                qrCode.setTracking(1L);
            else
                qrCode.setTracking(qrCode.getTracking() + 1);
            return qrCodeRepository.save(qrCode).getTracking();
        }
        return null;
    }

    public String updateFeedback(String code, String feedback) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElse(null);
        if (qrCode == null) {
            return "QRCode not found!";
        }
        qrCode.setFeedback(feedback);
        qrCodeRepository.save(qrCode);
        return "Update feedback for " + qrCode.getProduct().getName() + " successfully!";
    }

    @Transactional
    public ResponseModel getAllProductForDistributor() {
        List<Product> products = productRepository.findAllByOrderByIdAsc();
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProductToModel(products);
        productModels.forEach(productModel -> {
            Predicate<QRCodeModel> isNotAVAILABLE = qrCodeModel -> (qrCodeModel.getStatusQRCode() != StatusQRCode.AVAILABLE);
            productModel.getCodes().removeIf(isNotAVAILABLE);
        });
        Predicate<ProductModel> notContainsAVAILABLE = productModel -> (productModel.getCodes().size() == 0);
        productModels.removeIf(notContainsAVAILABLE);
        return new ResponseModel("List Products For Market", HttpStatus.OK.value(), productModels);
    }

    @Transactional
    public ResponseModel getAllOrderByProducer() {
        List<Producer> producers = producerRepository.findAll();
        List<ProducerModel> producerModels = new ArrayList<>();
        producers.forEach(producer -> {
            if (producer.getUser().isActive() && producer.getProducts() != null) {
                ProducerModel producerModel = ProducerMapper.INSTANCE.producerToProducerModel(producer);
                producerModel.getProductModels().forEach(productModel -> {
                    Predicate<QRCodeModel> isAVAILABLE = qrCodeModel -> (qrCodeModel.getStatusQRCode() != StatusQRCode.AVAILABLE);
                    productModel.getCodes().removeIf(isAVAILABLE);
                });
                Predicate<ProductModel> nullProductAVAILABLE = productModel -> (productModel.getCodes().size() == 0);
                producerModel.getProductModels().removeIf(nullProductAVAILABLE);
                producerModels.add(producerModel);
            }
        });
        Predicate<ProducerModel> nullProducerAVAILABLE = producerModel -> (producerModel.getProductModels().size() == 0);
        producerModels.removeIf(nullProducerAVAILABLE);
        return new ResponseModel("Get all product successfully", 200, producerModels);
    }
}
