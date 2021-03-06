package com.duytran.kdtrace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Getter
@Setter
@MappedSuperclass
public abstract class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 50)
    private String companyName;

    @Email
    private String email;

    private String address;

    @Size(max = 11)
    private String phone;

    private String avatar;

    private String website;

    private String tin;

    @Column(name = "rate", nullable = false, columnDefinition = "float default 5.0")
    private float rate = 5F;

    @Column(name = "count_rate", nullable = false, columnDefinition = "int default 1")
    private int countRate = 1;

    @Column(updatable = false)
    private String create_at;

    private String update_at;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", updatable = false)
    private User user;

    public Company(){
    }

    public void updateInformation(String companyName, String email, String address, String phone, String avatar, String update_at, String website, String tin){
        this.companyName = companyName;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.avatar = avatar;
        this.update_at = update_at;
        this.website = website;
        this.tin = tin;
    }

    public void updateRate(Float rate) {
        this.rate = (this.rate * countRate + rate)/(countRate + 1);
        countRate = countRate + 1;
    }
}
