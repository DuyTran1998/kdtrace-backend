package com.duytran.kdtrace.model;

import com.duytran.kdtrace.entity.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserModel {

    private Long id;

    private String username;

    @JsonIgnore
    private String password;

    private boolean isActive;

    private String email;

    private Role role;

    private String createAt;

    private Float rate;
}
