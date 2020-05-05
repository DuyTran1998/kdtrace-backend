package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String type;

    private Date mfg;

    private Date exp;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<QRCode> codes;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    private long quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Process> processes;
}