package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.exeption.RecordHasCreatedException;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.LedgerMapper;
import com.duytran.kdtrace.mapper.UserContextMapper;
import com.duytran.kdtrace.dto.UserContextDto;
import com.duytran.kdtrace.repository.HFUserContextRepository;
import com.duytran.kdtrace.repository.ProductRepositoty;
import com.duytran.kdtrace.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import main.HyperledgerFabric;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BlockchainService {

    private final HyperledgerFabric hyperledgerFabric;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private HFUserContextRepository hfUserContextRepository;
    @Autowired
    private ProductRepositoty productRepositoty;

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

    public boolean updateProducer(User user, Producer producer, String channelName) {
        try {
            LedgerProducer ledgerProducer = LedgerMapper.INSTANCE.toLedgerProducer(producer);
            ledgerProducer.setUserId(user.getId());
            ledgerProducer.setUsername(user.getUsername());
            ledgerProducer.setRole(user.getRole().getRoleName().name());
            return hyperledgerFabric.updateProducer(user, ledgerProducer, producer.getOrgMsp(), channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("updateProducer: exception");
        }
    }

    public boolean updateTransport(User user, Transport transport, String channelName) {
        try {
            LedgerTransport ledgerTransport = LedgerMapper.INSTANCE.toLedgerTransport(transport);
            ledgerTransport.setUserId(user.getId());
            ledgerTransport.setUsername(user.getUsername());
            ledgerTransport.setRole(user.getRole().getRoleName().name());
            return hyperledgerFabric.updateTransport(user, ledgerTransport, transport.getOrgMsp(), channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("updateTransport: exception");
        }
    }

    public boolean updateDistributor(User user, Distributor distributor, String channelName) {
        try {
            LedgerDistributor ledgerDistributor = LedgerMapper.INSTANCE.toLedgerDistributor(distributor);
            ledgerDistributor.setUserId(user.getId());
            ledgerDistributor.setUsername(user.getUsername());
            ledgerDistributor.setRole(user.getRole().getRoleName().name());
            return hyperledgerFabric.updateDistributor(user, ledgerDistributor, distributor.getOrgMsp(), channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("updateDistributor: exception");
        }
    }


    public boolean updateProduct(User user, Long productId, String channelName) {
        try {
            Product product = productRepositoty.findProductById(productId).get();
            LedgerProduct ledgerProduct = LedgerMapper.INSTANCE.toLedgerProduct(product);
            ledgerProduct.setUnit(product.getUnit().toString());
            List<Long> listQRCodeId = new ArrayList<>();
            product.getCodes().forEach(qrCode -> listQRCodeId.add(qrCode.getId()));
            ledgerProduct.setCodes(listQRCodeId);
            return hyperledgerFabric.updateProduct(user, ledgerProduct, product.getProducer().getOrgMsp(), channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("update Product: exception");
        }
    }

    public boolean createQRCodes(User user, Long productId, String channelName) {
        try {
            Product product = productRepositoty.findProductById(productId).get();
            List<LedgerQRCode> ledgerQRCodeList = LedgerMapper.INSTANCE.toLedgerQrCodeList(product.getCodes());
            return hyperledgerFabric.createQRCodes(user, ledgerQRCodeList, product.getProducer().getOrgMsp(), channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("update Product: exception");
        }
    }

    public boolean updateQRCodes(User user, List<Long> qrCodeIds, StatusQRCode statusQRCode, String orgMsp, String channelName) {
        try {
            return hyperledgerFabric.updateQRCodes(user, qrCodeIds, statusQRCode.name(), orgMsp, channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("update Product: exception");
        }
    }

    public boolean createProcess(User user, Process process, String channelName) {
        try {
            LedgerProcess ledgerProcess = LedgerMapper.INSTANCE.toLedgerProcess(process);
            ledgerProcess.setStatusProcess(process.getStatusProcess().name());

//            List<Long> listQRCodeId = new ArrayList<>();
//            process.getQrCodes().forEach(qrCode -> listQRCodeId.add(qrCode.getId()));
//            ledgerProcess.setQrCodes(listQRCodeId);
//
//            LedgerDeliveryTruck deliveryTruck = LedgerMapper.INSTANCE.toLedgerDeliveryTruck(process.getDeliveryTruck());
//            deliveryTruck.setAutoMaker(process.getDeliveryTruck().getAutoMaker().name());
//            deliveryTruck.setStatus(process.getDeliveryTruck().getStatus().name());
//            ledgerProcess.setDeliveryTruck(deliveryTruck);
            return hyperledgerFabric.createProcess(user, ledgerProcess, "Org3", channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("update Product: exception");
        }
    }

    public boolean updateProcess(User user, Long processId, StatusProcess statusProcess, List<Long> qrCodes, String channelName) {
        try {
            String orgMsp = "Org1"; //default
            switch (statusProcess) {
                case WAITING_RESPONSE_PRODUCER:
                    orgMsp = "Org3";
                    break;
                case PRODUCER_REJECT:
                    orgMsp = "Org1";
                    break;
                case CHOOSE_DELIVERYTRUCK_TRANSPORT:
                    orgMsp = "Org1";
                    break;
                case TRANSPORT_REJECT:
                    orgMsp = "Org2";
                    break;
                case ON_BOARDING_GET:
                    orgMsp = "Org1";
                    break;
                case ON_BOARDING_REVEIVE:
                    orgMsp = "Org2";
                    break;
                case REVEIVED:
                    orgMsp = "Org1";
                    break;
            }
            return hyperledgerFabric.updateProcess(user, processId, statusProcess.name(), qrCodes, orgMsp, channelName);
        } catch (Exception e) {
            throw new RecordHasCreatedException("update Product: exception");
        }
    }
}
