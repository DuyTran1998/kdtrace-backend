package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.ProductModel;
import com.duytran.kdtrace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductModel productModel){
        return ResponseEntity.ok(productService.createProduct(productModel));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

}