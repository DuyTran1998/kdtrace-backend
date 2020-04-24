package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.Producer;
import com.duytran.kdtrace.entity.Product;
import com.duytran.kdtrace.entity.QRCode;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.ProductMapper;
import com.duytran.kdtrace.model.ProductModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.ProducerRepository;
import com.duytran.kdtrace.repository.ProductRepositoty;
import com.duytran.kdtrace.repository.QRCodeRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
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
    ProducerRepository producerRepository;

    @Autowired
    UserPrincipalService userPrincipalService;

    @Transactional
    public ResponseModel createProduct(ProductModel productModel){
        Product product = ProductMapper.INSTANCE.productModelToProduct(productModel);
        Producer producer = producerRepository.findProducerByUser_Username(userPrincipalService.getUserCurrentLogined()).orElseThrow(
                ()-> new RecordNotFoundException("Producer is not exist")
        );
        product.setProducer(producer);
        productRepository.save(product);
        generateCode(product);
        return new ResponseModel("Create Successfully", HttpStatus.OK.value(), product);
    }

    public void generateCode(Product product){
        IntStream.rangeClosed(1,(int)product.getQuantity()).forEach(
                i -> qrCodeRepository.save(new QRCode(product,
                        product.getName() + "-L" + product.getId() +"-N"+ i,
                        userPrincipalService.getUserCurrentLogined()))
        );
    }

    @Transactional
    public ResponseModel getAllProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductModel> productModels = ProductMapper.INSTANCE.listProducToModel(products);
        return new ResponseModel("List Model", HttpStatus.OK.value(), productModels);
    }


    // Function to create Producer Detail.

    public void createProducer(User user){
        Producer producer = new Producer();
        producer.setCompanyName("Producer");
        producer.setUser(user);
        producerRepository.save(producer);
    }
}