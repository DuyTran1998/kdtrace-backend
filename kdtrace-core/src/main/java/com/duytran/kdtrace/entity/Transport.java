package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "transports")
public class Transport extends Company {

    @OneToMany(mappedBy = "transport", cascade = CascadeType.ALL)
    private List<DeliveryTruck> deliveryTruckList;
}