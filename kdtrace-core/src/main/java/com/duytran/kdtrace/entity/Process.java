package com.duytran.kdtrace.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "processes")
@EntityListeners(AuditingEntityListener.class)
public class Process {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "distributor_id")
    private Distributor distributor;

    private Long productID;

    private Long transportID;

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

    private String create_at;

    @LastModifiedDate
    private LocalDateTime updateAt;

    private boolean delFlag;

    public Process(){

    }

    public Process(Distributor distributor, Long productID, long quanlity, StatusProcess statusProcess, String create_at){
        this.distributor = distributor;
        this.productID = productID;
        this.quanlity = quanlity;
        this.statusProcess = statusProcess;
        this.create_at = create_at;
    }
}
