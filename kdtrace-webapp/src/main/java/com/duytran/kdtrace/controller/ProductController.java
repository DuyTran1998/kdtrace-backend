package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.ProductModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("get")
    public ResponseEntity<?> getProductById(@RequestParam Long id){
        return ResponseEntity.ok(productService.getProductById(id));
    }

//    @PostMapping("/create")
//    public ResponseEntity<?> createProduct(
//            @Valid @RequestBody ProductModel productModel,
//            @RequestPart("file") MultipartFile[] multipartFiles) throws IOException {
//        return ResponseEntity.ok(productService.createProduct(productModel, multipartFiles));
//    }
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE )
    public ResponseEntity<?> createProduct( @RequestPart ProductModel productModel,
                                             @RequestPart(value = "file0", required = false) MultipartFile[] multipartFiles,
                                            @RequestPart(value = "file1", required = false) MultipartFile[] multipartFiles1,
                                            @RequestPart(value = "file2", required = false) MultipartFile[] multipartFiles2) throws IOException {
        return ResponseEntity.ok(productService.createProduct(productModel, multipartFiles));
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/getAllOrderByProducer")
    public ResponseModel getAllOrderByProducer() {
        return productService.getAllOrderByProducer();
    }

    @GetMapping("getWithAvailable")
    public ResponseEntity<?> getProductByIdWithAvailable(@RequestParam Long id){
        return ResponseEntity.ok(productService.getProductByIdWithAvailable(id));
    }
}
