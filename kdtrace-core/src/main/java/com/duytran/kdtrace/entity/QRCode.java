package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "QRCodes")
public class QRCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private String qr_code;

    private String ower;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "process_id")
    private Process process;

    public QRCode(){
    }

    public QRCode(Product product, String qr_code, String ower){
        this.product = product;
        this.qr_code = qr_code;
        this.ower = ower;
    }
}
