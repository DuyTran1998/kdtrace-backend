package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.ProducerInfoModel;
import com.duytran.kdtrace.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/producer")
@PreAuthorize(("hasRole('ROLE_PRODUCER')"))
public class ProducerController {

    @Autowired
    private ProducerService producerService;

    @GetMapping("/get")
    public ResponseEntity<?> getProducer(){
        return ResponseEntity.ok(producerService.getProducer());
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateProducer(@RequestBody ProducerInfoModel producerInfoModel){
        return ResponseEntity.ok(producerService.updateProducer(producerInfoModel));
    }
}
