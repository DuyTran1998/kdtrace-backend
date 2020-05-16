package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.Unit;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ProductModel {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String type;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date mfg;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date exp;

    private List<QRCodeModel> codes;

    @NotNull
    private Long quantity;

    @Enumerated(EnumType.STRING)
    private Unit unit;
}