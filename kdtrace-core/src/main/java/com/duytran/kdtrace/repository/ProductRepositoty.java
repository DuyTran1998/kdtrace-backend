package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepositoty extends JpaRepository<Product, Long> {
    List<Product> findAllByProducer_Id(Long id);
}