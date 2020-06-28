package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Distributor;
import com.duytran.kdtrace.entity.Process;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    Optional<Process> findProcessById(Long id);

    List<Process> findProcessesByDistributor(Distributor distributor);
}
