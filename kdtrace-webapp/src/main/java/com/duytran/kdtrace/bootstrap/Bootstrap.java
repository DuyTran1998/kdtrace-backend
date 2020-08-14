package com.duytran.kdtrace.bootstrap;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.repository.HFUserContextRepository;
import com.duytran.kdtrace.repository.UserRepository;
import com.duytran.kdtrace.service.BlockchainService;
import com.duytran.kdtrace.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Slf4j
@AllArgsConstructor
class Bootstrap implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private HFUserContextRepository hfUserContextRepository;
    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        syncAdmin();
    }

    private void syncAdmin() {
        HFUserContext adminOrg1 = hfUserContextRepository.findByNameEqualsAndMspIdEquals("admin", "Org1MSP").orElse(null);
        if (adminOrg1 == null){
            try {
                log.info("Enroll admin Org1 into Blockchain");
                blockchainService.enrollAdmin("Org1");
            }catch (Exception e){
                log.info("Enroll admin Org1 into Blockchain fail: \n" + e);
            }
        }
        HFUserContext adminOrg2 = hfUserContextRepository.findByNameEqualsAndMspIdEquals("admin", "Org2MSP").orElse(null);
        if (adminOrg2 == null){
            try {
                log.info("Enroll admin Org1 into Blockchain");
                blockchainService.enrollAdmin("Org2");
            }catch (Exception e){
                log.info("Enroll admin Org2 into Blockchain fail: \n" + e);
            }
        }
        HFUserContext adminOrg3 = hfUserContextRepository.findByNameEqualsAndMspIdEquals("admin", "Org3MSP").orElse(null);
        if (adminOrg3 == null){
            try {
                log.info("Enroll admin Org1 into Blockchain");
                blockchainService.enrollAdmin("Org3");
            }catch (Exception e){
                log.info("Enroll admin Org3 into Blockchain fail: \n" + e);
            }
        }

        User user1 = userRepository.findByUsername("enduser-kdtrace1").orElse(
                new User("enduser-kdtrace1", passwordEncoder.encode("123456"), "kdtrace@gmail.com"));
        if(user1.getHfUserContext() == null || user1.getHfUserContext().getCertificate() != adminOrg1.getCertificate()){
            user1.setHfUserContext(adminOrg1);
            userRepository.save(user1);
        }
        User user2 = userRepository.findByUsername("enduser-kdtrace2").orElse(
                new User("enduser-kdtrace2", passwordEncoder.encode("123456"), "kdtrace@gmail.com"));
        if(user2.getHfUserContext() == null || user2.getHfUserContext().getCertificate() != adminOrg2.getCertificate()){
            user2.setHfUserContext(adminOrg2);
            userRepository.save(user2);
        }
        User user3 = userRepository.findByUsername("enduser-kdtrace3").orElse(
                new User("enduser-kdtrace3", passwordEncoder.encode("123456"), "kdtrace@gmail.com"));
        if (user3.getHfUserContext() == null || user3.getHfUserContext().getCertificate() != adminOrg3.getCertificate()){
            user3.setHfUserContext(adminOrg3);
            userRepository.save(user3);
        }
    }
}
