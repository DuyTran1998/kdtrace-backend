package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.ProductMapper;
import com.duytran.kdtrace.model.ProductModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.ProductRepositoty;
import com.duytran.kdtrace.repository.QRCodeRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

    @Transactional
    public ResponseModel createProduct(ProductModel productModel) {
        Product product = ProductMapper.INSTANCE.productModelToProduct(productModel);
        Producer producer = producerService.getProducerInPrincipal();
        product.setProducer(producer);
        producer.setCreate_at(commonService.getDateTime());
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
                    String code = product.getName() + "-L" + product.getId() + "-N" + i;
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
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProducToModel(products);
        return new ResponseModel("List Product", HttpStatus.OK.value(), productModels);
    }

    @Transactional
    public boolean checkQuanlityProducts(Long id_product, long quanlity){
        long quanlity_products = productRepository.getQuanlityProducts(id_product);
        return (quanlity_products >= quanlity);
    }

    @Transactional
    public Product getProductEntityById(Long id){
        return productRepository.findProductById(id).orElseThrow(
                () -> new RecordNotFoundException("Product is not exist")
        );
    }

    public ResponseModel getProductById(Long id){
        Product product = getProductEntityById(id);
        ProductModel productModel = ProductMapper.INSTANCE.productToProductModel(product);
        return new ResponseModel("Successfully", HttpStatus.OK.value(), productModel);
    }

    @Transactional
    public void changeStatusQRCode(User user, Long productID, long quanlity, StatusQRCode statusQRCode){
        List<QRCode> qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(productID, "AVAILABLE");
        List<Long> qrCodeIds = new ArrayList<>();
        IntStream.rangeClosed(0, (int)quanlity - 1).forEach(
                i ->{
                    QRCode qrCode = qrCodes.get(i);
                    qrCode.setStatusQRCode(statusQRCode);
                    qrCodeIds.add(qrCode.getId());
                    qrCodeRepository.save(qrCode);
                });
        blockchainService.saveQRCodes(user, qrCodeIds, statusQRCode, null, "Org1","kdtrace");
    }

    public boolean checkExistProductByIdAndProducer(Long id, Long producer_id){
        return productRepository.existsByIdAndProducer_Id(id, producer_id);
    }
}
