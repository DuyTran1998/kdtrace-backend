package main;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.User;
import lombok.extern.slf4j.Slf4j;
import model.*;
import service.CAIdentityService;
import service.LedgerProductService;
import service.LedgerUserService;
import util.Util;

import java.util.List;

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
    public boolean updateDistributor(User user, LedgerDistributor ledgerDistributor, String organization, String channelName) throws Exception {
        return ledgerUserService.updateDistributor(user, ledgerDistributor, organization, channelName);
    }
    public boolean updateProduct(User user, LedgerProduct ledgerProduct, String organization, String channelName) throws Exception {
        return ledgerProductService.updateProduct(user, ledgerProduct, organization, channelName);
    }
    public boolean updateQRCodes(User user, List<LedgerQRCode> qrCodeList, String organization, String channelName) throws Exception {
        return ledgerProductService.updateQRCodes(user, qrCodeList, organization, channelName);
    }
    public boolean updateProcess(User user, LedgerProcess ledgerProcess, String organization, String channelName) throws Exception {
        return ledgerProductService.updateProcess(user, ledgerProcess, organization, channelName);
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
