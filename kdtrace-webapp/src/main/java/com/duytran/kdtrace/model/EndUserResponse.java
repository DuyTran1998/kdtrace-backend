package com.duytran.kdtrace.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EndUserResponse {

    private ProductModel productModel;

    private ProducerModel producerModer;

    private String time1;

    private TransportModel transportModel;

    private DeliveryTruckModel deliveryTruckModel;

    private String time2;

    private DistributorModel distributorModel;
}