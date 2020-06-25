package com.duytran.kdtrace.dto;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.Role;
import com.duytran.kdtrace.entity.RoleName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private boolean isActive;
    private RoleName role;
}
