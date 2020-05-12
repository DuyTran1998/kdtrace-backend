package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.Producer;
import com.duytran.kdtrace.entity.Product;
import com.duytran.kdtrace.entity.QRCode;
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
    BlockchainService blockchainService;

    @Transactional
    public ResponseModel createProduct(ProductModel productModel) {
        Product product = ProductMapper.INSTANCE.productModelToProduct(productModel);
        Producer producer = producerService.getProducerInPrincipal();
        product.setProducer(producer);
        productRepository.save(product);
        List<QRCode> qrCodes = generateCode(product);
        qrCodeRepository.saveAll(qrCodes);
        product.setCodes(qrCodes);
        productRepository.save(product);
        blockchainService.updateProduct(producer.getUser(), product.getId(), "kdtrace");
        blockchainService.updateQRCodes(producer.getUser(), product.getId(), "kdtrace");
        return new ResponseModel("Create Successfully", HttpStatus.OK.value(), productModel);
    }

    @Value("${url}")
    private String url;

    public List<QRCode> generateCode(Product product) {
        List<QRCode> qrCodes = new ArrayList<>();
        IntStream.rangeClosed(1, product.getQuantity()).forEach(
                i -> qrCodes.add(new QRCode(product, url +
                        product.getName() + "-L" + product.getId() + "-N" + i,
                        product.getProducer().getCompanyName()))
        );
        return qrCodes;
    }

    @Transactional
    public ResponseModel getAllProducts() {
        List<Product> products = productRepository.findAllByProducer_Id(producerService.getProducerInPrincipal().getId());
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProducToModel(products);
        return new ResponseModel("List Model", HttpStatus.OK.value(), productModels);
    }

    @Transactional
    public boolean checkQuanlityProducts(Long id_product, long quanlity) {
        long quanlity_products = productRepository.getQuanlityProducts(id_product);
        if (quanlity_products >= quanlity) {
            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public Product getProductById(Long id) {
        Product product = productRepository.findProductById(id).orElseThrow(
                () -> new RecordNotFoundException("Product is not exist")
        );
        return product;
    }
}
