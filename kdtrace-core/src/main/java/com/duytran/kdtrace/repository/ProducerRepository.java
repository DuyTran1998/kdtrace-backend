package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Producer;
import org.hibernate.sql.Select;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {

    Optional<Producer> findProducerByUser_Username(String username);

    Optional<Producer> findProducerById(Long id);

    @Query(value = "SELECT producers.email from producers where producers.id in " +
            "(SELECT products.producer_id from products where products.id = ?1)", nativeQuery = true)
    String getProducerEmailByProductId(Long id);

    @Query(value = "SELECT producers.address from producers where producers.id in " +
            "(SELECT products.producer_id from products where products.id = ?1)", nativeQuery = true)
    String getProducerAddressByProductId(Long id);
}

