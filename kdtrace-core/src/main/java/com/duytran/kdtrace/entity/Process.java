package com.duytran.kdtrace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "processes")
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "distributor_id")
    private Distributor distributor;

    private Long productID;

    private long quanlity;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "process", fetch = FetchType.LAZY)
    private List<QRCode> qrCodes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "deliveryTruck_id")
    private DeliveryTruck deliveryTruck;

    @Enumerated(EnumType.STRING)
    private StatusProcess statusProcess;

    private String delivery_at;

    private String receipt_at;

    public Process(){

    }

    public Process(Distributor distributor, Long productID, long quanlity, StatusProcess statusProcess){
        this.distributor = distributor;
        this.productID = productID;
        this.quanlity = quanlity;
        this.statusProcess = statusProcess;
    }
}
