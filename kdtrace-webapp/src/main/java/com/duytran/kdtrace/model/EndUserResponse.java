package com.duytran.kdtrace.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EndUserResponse {

    private ProductModel productModel;

    private ProducerModel producerModel;

    private String delivery_time;

    private TransportModel transportModel;

    private DeliveryTruckModel deliveryTruckModel;

    private String receipt_time;

    private DistributorModel distributorModel;

    public EndUserResponse(){

    }

    public EndUserResponse(ProductModel productModel, ProducerModel producerModel, String delivery_time,
                           TransportModel transportModel, DeliveryTruckModel deliveryTruckModel, String receipt_time,
                           DistributorModel distributorModel){
        this.productModel = productModel;
        this.producerModel = producerModel;
        this.delivery_time = delivery_time;
        this.transportModel = transportModel;
        this.deliveryTruckModel = deliveryTruckModel;
        this.receipt_time = receipt_time;
        this.distributorModel = distributorModel;
    }
}