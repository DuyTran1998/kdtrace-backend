package com.duytran.kdtrace.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "distributors")
public class Distributor extends Company {
    @OneToMany(mappedBy = "distributor", fetch = FetchType.LAZY)
    private List<Process> processes;

    String orgMsp = "Org3";
}
