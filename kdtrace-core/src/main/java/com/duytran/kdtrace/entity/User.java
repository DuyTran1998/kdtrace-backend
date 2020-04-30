package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    @Column(updatable = false)
    private String username;

    @JsonIgnore
    @NotBlank
    @Size(min = 3, max = 100)
    private String password;

    private boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private Role role;

    public User(){}

    public User(String username, String password, boolean isActive, Role role){
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }
}