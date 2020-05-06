package com.duytran.kdtrace.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "producers")
public class Producer extends Company {

    @OneToMany(mappedBy = "producer", fetch = FetchType.LAZY)
    private List<Product> products;

    String orgMsp = "Org1";
}
