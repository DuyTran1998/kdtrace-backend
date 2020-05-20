package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Distributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DistributorRepository extends JpaRepository<Distributor, Long> {
    Optional<Distributor> findDistributorByUser_Username(String username);

    boolean existsById(Long id);

    Optional<Distributor> findDistributorById(Long id);
}