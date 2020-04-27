package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "QRCodes")
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private String qr_code;

    private String ower;

    public QRCode(){
    }

    public QRCode(Product product, String qr_code, String ower){
        this.product = product;
        this.qr_code = qr_code;
        this.ower = ower;
    }
}