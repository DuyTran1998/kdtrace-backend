package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.Automaker;
import com.duytran.kdtrace.entity.StatusDeliveryTruck;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
public class DeliveryTruckModel {

    private Long id;

    private String numberPlate;

    @Enumerated(EnumType.STRING)
    private Automaker autoMaker;

    @Enumerated(EnumType.STRING)
    private StatusDeliveryTruck statusDeliveryTruck;
}
