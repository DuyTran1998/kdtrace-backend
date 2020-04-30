package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordHasCreatedException;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.UserContextMapper;
import com.duytran.kdtrace.model.UserContextDto;
import com.duytran.kdtrace.repository.HFUserContextRepository;
import com.duytran.kdtrace.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import main.HyperledgerFabric;
import model.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class BlockchainService {

    private final HyperledgerFabric hyperledgerFabric;
    private final UserRepository userRepository;
    private final HFUserContextRepository hfUserContextRepository;

    @Autowired
    public BlockchainService(UserRepository userRepository, HFUserContextRepository hfUserContextRepository) {
        this.hyperledgerFabric = HyperledgerFabric.newInstance();
        this.userRepository = userRepository;
        this.hfUserContextRepository = hfUserContextRepository;
    }

    public UserContextDto enrollAdmin(String organization) {
        UserContext userContext;
        try {
            userContext = hyperledgerFabric.enrollAdmin(organization);
        } catch (Exception e) {
            throw new RuntimeException("Enrolling admin fail");
        }
        HFUserContext hfUserContext;
        try {
            hfUserContext = hfUserContextRepository
                    .findByNameEqualsAndMspIdEquals("admin", organization)
                    .orElse(new HFUserContext());
            hfUserContext
                    .name(userContext.getName())
                    .account(userContext.getAccount())
                    .affiliation(userContext.getAffiliation())
                    .mspId(userContext.getMspId())
                    .encodedPrivateKey(userContext.getEnrollment().getKey().getEncoded())
                    .certificate(userContext.getEnrollment().getCert())
                    .user(null);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot enroll admin identity");
        }
        hfUserContextRepository.save(hfUserContext);
        return UserContextMapper.INSTANCE.toUserContextDto(hfUserContext);
    }

    /* Need enroll admin firstly */
    public UserContextDto registerIdentity(String username, String organization) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RecordNotFoundException("Cannot find user"));

        boolean isExisted = hfUserContextRepository.findByUserId(user.getId()).isPresent();
        if (isExisted) {
            log.info("User is already registered. Therefore skipping..... registeration");
            throw new RecordHasCreatedException("Not valid, User is registered");
        }
        HFUserContext adminHfUserContext = hfUserContextRepository
                .findByNameEqualsAndAffiliationEquals("admin", organization)
                .orElseThrow(() -> new RecordNotFoundException("Cannot find admin context"));
        HFUserContext hfUserContext;
        try {
            UserContext userContext = hyperledgerFabric.registerIdentity(organization, user, adminHfUserContext);
            hfUserContext = UserContextMapper.INSTANCE.toHFUserContext(userContext)
                    .encodedPrivateKey(userContext.getEnrollment().getKey().getEncoded())
                    .certificate(userContext.getEnrollment().getCert())
                    .user(user);
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot register identity");
        }
        userRepository.save(user.hfUserContext(hfUserContext));
        return UserContextMapper.INSTANCE.toUserContextDto(hfUserContext);
    }
}
