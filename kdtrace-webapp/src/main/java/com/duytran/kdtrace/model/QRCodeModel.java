package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.StatusQRCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QRCodeModel {

    private Long id;

    private String code;

    private String ower;

    private String link;

    private StatusQRCode statusQRCode;
}
