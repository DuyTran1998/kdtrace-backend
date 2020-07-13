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
public class TransportController {
    @Autowired
    private TransportService transportService;

    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_TRANSPORT')")
    public ResponseEntity<?> getTransport(){
        return ResponseEntity.ok(transportService.getTransport());
    }

    @GetMapping("getAll")
    public ResponseEntity<?> getAllTransportCompany(){
        return ResponseEntity.ok(transportService.getAllTransports());
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ROLE_TRANSPORT')")
    public ResponseEntity<?> updateTransport(@Valid @RequestBody TransportModel transportModel){
        return ResponseEntity.ok(transportService.updateTransport(transportModel));
    }

    @PostMapping("/deliveryTruck/create")
    @PreAuthorize("hasRole('ROLE_TRANSPORT')")
    public ResponseEntity<?> createDeliveryTruck(@Valid @RequestBody DeliveryTruckModel deliveryTruckModel){
        return ResponseEntity.ok(transportService.createDeliveryTruck(deliveryTruckModel));
    }

    @GetMapping("/deliveryTruck/getAll")
    @PreAuthorize("hasRole('ROLE_TRANSPORT')")
    public ResponseEntity<?> getAllDeveiryTruck(){
        return ResponseEntity.ok(transportService.getDeliveryTruckList());
    }

    @GetMapping("/deliveryTruck/getAllAvailable")
    @PreAuthorize("hasRole('ROLE_TRANSPORT')")
    public ResponseEntity<?> getAllDeveiryTruckIsAvailable(){
        return ResponseEntity.ok(transportService.getDeliveryTruckListAvailable());
    }
}
