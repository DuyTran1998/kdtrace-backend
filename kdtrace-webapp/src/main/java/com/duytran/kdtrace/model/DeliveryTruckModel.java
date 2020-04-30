package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.Automaker;
import com.duytran.kdtrace.entity.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class DeliveryTruckModel {
    private Long id;

    private String numberPlate;

    @Enumerated(EnumType.STRING)
    private Automaker autoMaker;

    @Enumerated(EnumType.STRING)
    private Status status;
}