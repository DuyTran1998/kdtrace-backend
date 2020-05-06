package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.Producer;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordHasCreatedException;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.UserContextMapper;
import com.duytran.kdtrace.dto.UserContextDto;
import com.duytran.kdtrace.repository.HFUserContextRepository;
import com.duytran.kdtrace.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import main.HyperledgerFabric;
import model.LedgerProducer;
import model.UserContext;
import com.duytran.kdtrace.mapper.LedgerUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BlockchainService {

    private final HyperledgerFabric hyperledgerFabric;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HFUserContextRepository hfUserContextRepository;

    @Autowired
    public BlockchainService() {
        this.hyperledgerFabric = HyperledgerFabric.newInstance();
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

    public boolean updateProducer(User user, Producer producer , String channelName) {
        try {
            LedgerProducer ledgerProducer = LedgerUserMapper.INSTANCE.toLedgerProducer(producer);
            ledgerProducer.setUserId(user.getId());
            ledgerProducer.setUsername(user.getUsername());
            ledgerProducer.setRole(user.getRole().getRoleName().name());
            return hyperledgerFabric.updateProducer(user, ledgerProducer, producer.getOrgMsp(), channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("updateProducer: exception");
        }
    }
}
