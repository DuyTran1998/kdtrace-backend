package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.StatusProcess;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProcessModel {
    private Long id;

    private Long ProductID;

    private Long transportID;

    private StatusProcess statusProcess;

    private String delivery_at;

    private String receipt_at;

    private long quanlity;

    private String create_at;

    private List<QRCodeModel> qrCodeModels;

    private ProducerModel producerModel;

    private TransportModel transportModel;

    private DeliveryTruckModel deliveryTruckModel;

    private DistributorModel distributorModel;

    public void updateProcessModel(DistributorModel distributorModel, DeliveryTruckModel deliveryTruckModel,
                                   List<QRCodeModel> qrCodeModels, ProducerModel producerModel){
        this.distributorModel = distributorModel;
        this.deliveryTruckModel = deliveryTruckModel;
        this.qrCodeModels = qrCodeModels;
        this.producerModel = producerModel;
    }
}
