package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.DeliveryTruckModel;
import com.duytran.kdtrace.model.TransportModel;
import com.duytran.kdtrace.service.TransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/transport")
@PreAuthorize("hasRole('ROLE_TRANSPORT')")
public class TransportController {
    @Autowired
    private TransportService transportService;

    @GetMapping("/get")
    public ResponseEntity<?> getTransport(){
        return ResponseEntity.ok(transportService.getTransport());
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateTransport(@Valid @RequestBody TransportModel transportModel){
        return ResponseEntity.ok(transportService.updateTransport(transportModel));
    }

    @PostMapping("/deliveryTruck/create")
    public ResponseEntity<?> createDeliveryTruck(@Valid @RequestBody DeliveryTruckModel deliveryTruckModel){
        return ResponseEntity.ok(transportService.createDeliveryTruck(deliveryTruckModel));
    }

    @GetMapping("/deliveryTruck/getAll")
    public ResponseEntity<?> getAllDeveiryTruck(){
        return ResponseEntity.ok(transportService.getDeliveryTruckList());
    }
}