package com.duytran.kdtrace.repository;

import com.duytran.kdtrace.entity.HFUserContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface HFUserContextRepository extends JpaRepository<HFUserContext, Long> {

    @Transactional
    Optional<HFUserContext> findByName(String name);

    @Transactional
    Optional<HFUserContext> findByUserId(Long user_id);

    @Transactional
    Optional<HFUserContext> findByNameEqualsAndMspIdEquals(String name, String msp_id);

    @Transactional
    List<HFUserContext> findAllByMspId(String msp_id);

    @Transactional
    Optional<HFUserContext> findByNameEqualsAndAffiliationEquals(String name, String affiliation);
}
