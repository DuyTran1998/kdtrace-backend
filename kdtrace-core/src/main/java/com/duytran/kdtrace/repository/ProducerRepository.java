package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProducerRepository extends JpaRepository<Producer, Long> {
    Optional<Producer> findProducerByUser_Username(String username);
}
