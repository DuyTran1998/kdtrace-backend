package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.StatusProcess;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProcessModel {
    private Long id;

    private DistributorModel distributorModel;

    private Long ProductID;

    private DeliveryTruckModel deliveryTruckModel;

    private StatusProcess statusProcess;

    private String delivery_at;

    private String receipt_at;

    private long quanlity;

    private List<QRCodeModel> qrCodeModels;

    public void updateProcessModel(DistributorModel distributorModel, DeliveryTruckModel deliveryTruckModel,
                                   List<QRCodeModel> qrCodeModels){
        this.distributorModel = distributorModel;
        this.deliveryTruckModel = deliveryTruckModel;
        this.qrCodeModels = qrCodeModels;
    }
}