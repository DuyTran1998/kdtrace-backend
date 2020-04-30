package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "processes")
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "process")
    private List<QRCode> qrCodes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deliveryTruck_id")
    private DeliveryTruck deliveryTruck;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "distributor_id")
    private Distributor distributor;
}