package com.duytran.kdtrace.model;

import lombok.Data;

@Data
public class DistributorModel {
    private long id;
    private String companyName;
    private String email;
    private String address;
    private String phone;
    private String avatar;
    private String update_at;
}