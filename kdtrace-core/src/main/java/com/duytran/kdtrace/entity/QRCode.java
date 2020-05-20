package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "QRCodes")
public class QRCode implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")

    private Product product;

    private String code;

    private String ower;

    private String link;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "process_id")
    private Process process;

    @Enumerated(EnumType.STRING)
    private StatusQRCode statusQRCode;

    private String create_at;


    public QRCode(){
    }


    public QRCode(Product product, String code, String ower, String link, StatusQRCode statusQRCode, String create_at){
        this.product = product;
        this.code = code;
        this.ower = ower;
        this.link = link;
        this.statusQRCode = statusQRCode;
        this.create_at = create_at;
    }
}
