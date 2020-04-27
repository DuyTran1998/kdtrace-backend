package com.duytran.kdtrace.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "distributors")
public class Distributor extends Company {
}