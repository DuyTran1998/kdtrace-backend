package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

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

    @Email
    private String email;

    private boolean isActive;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="role_id", referencedColumnName = "id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "hf_user_context_id", referencedColumnName = "id")
    private HFUserContext hfUserContext;

    private String organization = "Org1";

    public User(){

    }

    public User(String username, String password, boolean isActive, Role role){
        this.username = username;
        this.password = password;
        this.isActive = isActive;
        this.role = role;
    }

    public User(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User hfUserContext(HFUserContext hfUserContext) {
        this.hfUserContext = hfUserContext;
        return this;
    }
}
