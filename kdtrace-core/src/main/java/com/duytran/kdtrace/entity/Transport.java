package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "transports")
public class Transport extends Company {

    @OneToMany(mappedBy = "transport", fetch = FetchType.LAZY)
    private List<DeliveryTruck> deliveryTruckList;
}