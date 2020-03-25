package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    private long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(){
    }

    public Role(long id, RoleName roleName){
        this.id = id;
        this.roleName = roleName;
    }
}