package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    @JsonIgnore
    @OneToMany(targetEntity=User.class, mappedBy="role",cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> user = new ArrayList<>();

    public Role(){
    }

    public Role(RoleName roleName){
        this.roleName = roleName;
    }
}