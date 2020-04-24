package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Data
@Entity
@Table(name = "producers")
public class Producer extends Company {

    @OneToMany(mappedBy = "producer", cascade = CascadeType.ALL)
    private List<Product> products;
}