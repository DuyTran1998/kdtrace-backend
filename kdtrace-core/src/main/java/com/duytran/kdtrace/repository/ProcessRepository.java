package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.Distributor;
import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.entity.StatusProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
    Optional<Process> findProcessByDelFlagFalseAndId(Long id);

    List<Process> findProcessesByDelFlagFalseAndDistributor(Distributor distributor);
    List<Process> findByDelFlagFalseAndStatusProcessNotLikeAndProductIDIn(StatusProcess statusProcess, List<Long> productIdList);
    List<Process> findProcessesByDelFlagFalseAndStatusProcessNotLikeAndTransportID(StatusProcess statusProcess, Long transportID);
}
