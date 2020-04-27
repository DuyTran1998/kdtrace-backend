package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
        import javax.validation.constraints.NotBlank;

@Data
@Entity
@Table
public class DeliveryTruck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    @NotBlank
    private String numberPlate;

    @Enumerated(EnumType.STRING)
    private Automaker autoMaker;

    @Enumerated(EnumType.STRING)
    private Status status;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "transport_id")
    private Transport transport;

}