package com.duytran.kdtrace.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
        import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Entity
@Table
public class DeliveryTruck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank
    private String numberPlate;

    @Enumerated(EnumType.STRING)
    private Automaker autoMaker;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "transport_id")
    private Transport transport;

    @OneToMany(mappedBy = "deliveryTruck", fetch = FetchType.LAZY)
    private List<Process> processes;

}