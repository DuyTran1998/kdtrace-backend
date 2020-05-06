package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.QRCode;
import com.duytran.kdtrace.entity.Unit;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
public class ProductModel {
    private Long id;

    private String name;

    private String type;

    private Date mfg;

    private Date exp;

    private List<QRCodeModel> codes;

    private Long quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;
}