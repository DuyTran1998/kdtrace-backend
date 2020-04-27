package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
public class UserModel {
    private long id;
    private String username;
    @JsonIgnore
    private String password;
    private boolean isActive;
    private Role role;
}