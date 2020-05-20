package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepositoty extends JpaRepository<Product, Long> {
    List<Product> findAllByProducer_Id(Long id);

    @Query(value = "SELECT products.quantity from products where products.id=?", nativeQuery = true)
    long getQuanlityProducts(Long id_product);

    Optional<Product> findProductById(Long id);

    boolean existsByIdAndProducer_Id(Long id, Long producer_id);
}
