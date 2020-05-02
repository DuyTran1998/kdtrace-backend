package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "producers")
public class Producer extends Company {

    @OneToMany(mappedBy = "producer", fetch = FetchType.LAZY)
    private List<Product> products;

    String orgMsp = "Org1";
}
