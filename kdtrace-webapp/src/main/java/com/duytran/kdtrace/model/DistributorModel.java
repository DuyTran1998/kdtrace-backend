package com.duytran.kdtrace.model;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DistributorModel {
    private Long id;
    private String companyName;
    private String email;
    private String address;
    private String phone;
    private String avatar;
    private String update_at;
}