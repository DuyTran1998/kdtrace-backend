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

    @OneToMany(mappedBy = "transport", fetch = FetchType.LAZY)
    private List<DeliveryTruck> deliveryTruckList;

    String orgMsp = "Org2";
}
