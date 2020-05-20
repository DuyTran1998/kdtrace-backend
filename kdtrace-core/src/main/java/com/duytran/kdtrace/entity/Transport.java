package com.duytran.kdtrace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "transports")
public class Transport extends Company {

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL)
    private List<DeliveryTruck> deliveryTruckList;
}