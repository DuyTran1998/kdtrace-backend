package main;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.StatusProcess;
import com.duytran.kdtrace.entity.StatusQRCode;
import com.duytran.kdtrace.entity.User;
import lombok.extern.slf4j.Slf4j;
import model.*;
import service.CAIdentityService;
import service.LedgerProductService;
import service.LedgerUserService;
import util.Util;

import java.util.List;
import java.util.Map;

@Slf4j
public class HyperledgerFabric {

    private LedgerUserService ledgerUserService;
    private CAIdentityService caIdentityService;
    private LedgerProductService ledgerProductService;


    public static HyperledgerFabric newInstance() {
        return new HyperledgerFabric();
    }

    private HyperledgerFabric() {
        this.ledgerUserService = LedgerUserService.newInstance();
        this.caIdentityService = CAIdentityService.newInstance();
        this.ledgerProductService = LedgerProductService.newInstance();
    }

    public boolean updateProducer(User user, LedgerProducer ledgerProducer, String organization, String channelName) throws Exception {
        return ledgerUserService.updateProducer(user, ledgerProducer, organization, channelName);
    }

    public boolean updateTransport(User user, LedgerTransport ledgerTransport, String organization, String channelName) throws Exception {
        return ledgerUserService.updateTransport(user, ledgerTransport, organization, channelName);
    }

    public boolean updateDeliveryTruck(User user, LedgerDeliveryTruck ledgerDeliveryTruck, String organization, String channelName) throws Exception {
        return ledgerUserService.updateDeliveryTruck(user, ledgerDeliveryTruck, organization, channelName);
    }

    public boolean updateDistributor(User user, LedgerDistributor ledgerDistributor, String organization, String channelName) throws Exception {
        return ledgerUserService.updateDistributor(user, ledgerDistributor, organization, channelName);
    }

    public boolean updateProduct(User user, LedgerProduct ledgerProduct, String organization, String channelName) throws Exception {
        return ledgerProductService.updateProduct(user, ledgerProduct, organization, channelName);
    }

    public boolean createQRCodes(User user, List<LedgerQRCode> qrCodeList, String organization, String channelName) throws Exception {
        return ledgerProductService.createQRCodes(user, qrCodeList, organization, channelName);
    }

    public boolean saveQRCodes(User user, List<Long> qrCodeIds, String statusQRCode, Map<Long, String> mapOtp, String orgMsp, String channelName) throws Exception {
        return ledgerProductService.saveQRCodes(user, qrCodeIds, statusQRCode, mapOtp, orgMsp, channelName);
    }

    public boolean createProcess(User user, LedgerProcess ledgerProcess, String organization, String channelName) throws Exception {
        return ledgerProductService.createProcess(user, ledgerProcess, organization, channelName);
    }

    public boolean updateProcess(User user, Long processId, String statusProcess,
                                 List<Long> qrCodes, Long transportId, Long deliveryTruckId, String delivery_at, String receipt_at, String orgMsp, String channelName) throws Exception {
        return ledgerProductService.updateProcess(user, processId, statusProcess,
                qrCodes, transportId, deliveryTruckId, delivery_at, receipt_at, orgMsp, channelName);
    }

    public UserContext enrollAdmin(String organization) throws Exception {
        return caIdentityService.enrollAdmin(organization);
    }

    public UserContext registerIdentity(String organization, User user, HFUserContext adminHfUserContext) {
        UserContext adminContext = Util.toUserContextFromHFUserContext(adminHfUserContext);
        return caIdentityService.registerIdentity(organization, user, adminContext);
    }

//    public UserContext updateIdentity(String organizationName, User user, HFUserContext adminHfUserContext) {
//        UserContext adminContext = Util.toUserContextFromHFUserContext(adminHfUserContext);
//        String organization = Util.organizationFilter(organizationName);
//        try {
//            return new CAClientWrapper(organization).updateIdentity(adminContext, user);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error updating " + user.getUsername() + "'s identity into network");
//        }
//    }

}
