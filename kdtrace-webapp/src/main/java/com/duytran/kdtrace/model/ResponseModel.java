package com.duytran.kdtrace.model;

import lombok.*;

@Data
@AllArgsConstructor
public class ResponseModel {
    private String message;
    private int status;
    private Object result;
}