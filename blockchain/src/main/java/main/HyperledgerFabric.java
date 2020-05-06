package main;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.User;
import lombok.extern.slf4j.Slf4j;
import model.LedgerProducer;
import model.UserContext;
import service.CAIdentityService;
import service.LedgerUserService;
import util.Util;

@Slf4j
public class HyperledgerFabric {

    private LedgerUserService ledgerUserService;
    private CAIdentityService caIdentityService;



    public static HyperledgerFabric newInstance() {
        return new HyperledgerFabric();
    }

    private HyperledgerFabric() {
        this.ledgerUserService = LedgerUserService.newInstance();
        this.caIdentityService = CAIdentityService.newInstance();
    }

    public boolean updateProducer(User user, LedgerProducer ledgerProducer, String organization, String channelName) throws Exception {
        return ledgerUserService.updateProducer(user, ledgerProducer, organization, channelName);
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
