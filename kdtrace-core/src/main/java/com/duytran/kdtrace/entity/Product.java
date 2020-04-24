package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private String type;

    private Date mfg;

    private Date exp;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<QRCode> codes;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "producer_id")
    private Producer producer;

    private long quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

}