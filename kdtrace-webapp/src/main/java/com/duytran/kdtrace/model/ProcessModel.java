package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.StatusProcess;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

    private ProductModel productModel;

    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss")
    private LocalDateTime updateAt;



    public void updateProcessModel(DistributorModel distributorModel, DeliveryTruckModel deliveryTruckModel,
                                   List<QRCodeModel> qrCodeModels, ProducerModel producerModel){
        this.distributorModel = distributorModel;
        this.deliveryTruckModel = deliveryTruckModel;
        this.qrCodeModels = qrCodeModels;
        this.producerModel = producerModel;
    }
}
