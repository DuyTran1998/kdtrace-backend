package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.DistributorModel;
import com.duytran.kdtrace.service.DistributorService;
import com.duytran.kdtrace.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/distributor")
@PreAuthorize(("hasRole('ROLE_DISTRIBUTOR')"))
public class DistributorController {

    @Autowired
    private DistributorService distributorService;

    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getDistributor(){
        return ResponseEntity.ok(distributorService.getDistributor());
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateDistributor(@Valid @RequestBody DistributorModel distributorModel){
        return ResponseEntity.ok(distributorService.updateDistriButor(distributorModel));
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productService.getAllProductForDistributor());
    }

    @GetMapping("/getAllProducer")
    public ResponseEntity<?> getAllProducer(){
        return ResponseEntity.ok(productService.getAllProducerName());
    }
}
