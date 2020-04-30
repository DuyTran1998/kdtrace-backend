package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "distributors")
public class Distributor extends Company {
    @OneToMany(mappedBy = "distributor", fetch = FetchType.LAZY)
    private List<Process> processes;
}