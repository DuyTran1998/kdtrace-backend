package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.RequestProcessModel;
import com.duytran.kdtrace.service.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/process")
public class ProcessController {
    @Autowired
    private ProcessService processService;


    @GetMapping("/get")
    public ResponseEntity<?> getProcess(@RequestParam Long id){
        return ResponseEntity.ok(processService.getProcess(id));
    }


    @PreAuthorize(("hasRole('ROLE_DISTRIBUTOR')"))
    @PostMapping("/create")
    public ResponseEntity<?> createProcess(@Valid @RequestBody RequestProcessModel processModel){
        return ResponseEntity.ok(processService.createProcess(processModel));
    }


    @PreAuthorize(("hasRole('ROLE_PRODUCER')"))
    @PostMapping("/acceptToSell")
    public ResponseEntity<?> acceptToAgreementWithDistributor(@RequestParam Long id){
        return ResponseEntity.ok(processService.acceptToSell(id));
    }

    @PreAuthorize(("hasRole('ROLE_DISTRIBUTOR')"))
    @PostMapping("/chooseTransport")
    public ResponseEntity<?> chooseTransport(@RequestParam Long id, @RequestParam Long transport_id){
        return ResponseEntity.ok(processService.chooseTransport(id, transport_id));
    }


    @PreAuthorize(("hasRole('ROLE_TRANSPORT')"))
    @PostMapping("/confirmToGet")
    public ResponseEntity<?> confirmToDelivery(@RequestParam Long id){
        return ResponseEntity.ok(processService.confirmToGetGoods(id));
    }


    @PreAuthorize(("hasRole('ROLE_TRANSPORT')"))
    @PostMapping("/confirmToReceipt")
    public ResponseEntity<?> confirmToReceipt(@RequestParam Long id){
        return ResponseEntity.ok(processService.confirmToReceiptGoods(id));
    }


    @PreAuthorize(("hasRole('ROLE_TRANSPORT')"))
    @PostMapping("/acceptToDelivery")
    public ResponseEntity<?> acceptToDelivery(@RequestParam Long id, @RequestParam Long id_deliveryTruck){
        return ResponseEntity.ok(processService.acceptToDelivery(id, id_deliveryTruck));
    }
}