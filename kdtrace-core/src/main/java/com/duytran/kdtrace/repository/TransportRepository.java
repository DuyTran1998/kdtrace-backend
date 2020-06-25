package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Transport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportRepository extends JpaRepository<Transport,Long> {
    Optional<Transport> findTransportByUser_Username(String username);

    boolean existsById(Long id);

    Optional<Transport> findTransportById(Long id);

    List<Transport> findAll();

    @Query(value = "SELECT transports.email from transports where transports.id=?1", nativeQuery = true)
    String getEmailByTransport_Id(Long id);
}
