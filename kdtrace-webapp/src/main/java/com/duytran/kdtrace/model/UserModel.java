package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.CompanyInfo;
import com.duytran.kdtrace.entity.Role;
import lombok.Data;

@Data
public class UserModel {
    private long id;
    private String username;
    private String password;
    private Role role;
}