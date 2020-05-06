package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hf_user_context_id", referencedColumnName = "id")
    private HFUserContext hfUserContext;


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

    public User hfUserContext(HFUserContext hfUserContext) {
        this.hfUserContext = hfUserContext;
        return this;
    }
}
