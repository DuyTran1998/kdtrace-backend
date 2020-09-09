package com.duytran.kdtrace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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

    private LocalDate startDay;

    private LocalDate endDay;

    @Column
    private Float price;

    private String root;

    @OneToMany(mappedBy = "product")
    private List<Medicine> medicines;


    @OneToMany(mappedBy = "product")
    private List<QRCode> codes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    private long quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;

    private String create_at;

    @Column(name = "image", length = 2048)
    private String image;
}
