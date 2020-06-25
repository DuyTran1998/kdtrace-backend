package com.duytran.kdtrace.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseModel {

    private String message;

    private int status;

    private Object result;
}