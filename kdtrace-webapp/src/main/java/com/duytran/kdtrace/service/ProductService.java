package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.Producer;
import com.duytran.kdtrace.entity.Product;
import com.duytran.kdtrace.entity.QRCode;
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

    @Transactional
    public ResponseModel createProduct(ProductModel productModel) {
        Product product = ProductMapper.INSTANCE.productModelToProduct(productModel);
        Producer producer = producerService.getProducerInPrincipal();
        product.setProducer(producer);
        productRepository.save(product);
        generateCode(product);
        return new ResponseModel("Create Successfully", HttpStatus.OK.value(), product);
    }

    @Value("${url}")
    private String url;

    public void generateCode(Product product) {
        IntStream.rangeClosed(1, (int) product.getQuantity()).forEach(
                i -> qrCodeRepository.save(new QRCode(product, url +
                        product.getName() + "-L" + product.getId() + "-N" + i,
                        product.getProducer().getCompanyName()))
        );
    }

    @Transactional
    public ResponseModel getAllProducts() {
        List<Product> products = productRepository.findAllByProducer_Id(producerService.getProducerInPrincipal().getId());
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProducToModel(products);
        return new ResponseModel("List Model", HttpStatus.OK.value(), productModels);
    }
}