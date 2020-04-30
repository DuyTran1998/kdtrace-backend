package main;

import com.duytran.kdtrace.entity.HFUserContext;
import com.duytran.kdtrace.entity.User;
import lombok.extern.slf4j.Slf4j;
import model.UserContext;
import service.CAIdentityService;
//import service.LedgerTaskService;
import util.Util;

@Slf4j
public class HyperledgerFabric {

//    private LedgerTaskService ledgerTaskService;
    private CAIdentityService caIdentityService;



    public static HyperledgerFabric newInstance() {
        return new HyperledgerFabric();
    }

    private HyperledgerFabric() {
//        this.ledgerTaskService = LedgerTaskService.newInstance();
        this.caIdentityService = CAIdentityService.newInstance();
    }

//    public boolean shareProjects(List<Project> removedCurrentProjects, List<Project> addedCurrentProjects, User user, String organization, String channelName) throws Exception {
//        return ledgerProjectService.shareProjects(removedCurrentProjects, addedCurrentProjects, user, organization, channelName);
//    }
//
//    public boolean saveTask(User user, LedgerTask ledgerTask, String organization, String channelName) throws Exception {
//        return ledgerTaskService.saveTask(user, ledgerTask, organization, channelName);
//    }
//
//    public boolean saveRedmineTask(User user, LedgerRedmineTask ledgerRedmineTask, String organization, String channelName) throws Exception {
//        return ledgerTaskService.saveRedmineTask(user, ledgerRedmineTask, organization, channelName);
//    }
//
//    public LedgerDefaultTaskPagination getAllTasksPagination(int page, int size, String direction,
//                                                             String sort, String search, String org, User user,
//                                                             String channelName, String typeTask) {
//        return ledgerTaskService.getAllTasksPagination(page, size, direction, sort, search, org, user, channelName, typeTask);
//    }
//
//    public List<LedgerDefaultTaskTransaction> getDefaultTaskHistory(String key, String type, User user, String organization,
//                                                                    String channelName) {
//        return ledgerTaskService.getDefaultTaskHistory(key, type, user, organization, channelName);
//    }
//
//    public LedgerTaskPagination getTasksPagination(int page, int size, String direction,
//                                                   String sort, String search, String org, User user,
//                                                   String channelName) {
//        return ledgerTaskService.getTasksPagination(page, size, direction, sort, search, org, user, channelName);
//    }
//
//    public List<LedgerTask> getTasks(User user, String organization, String channelName) {
//        return ledgerTaskService.getTasks(user, organization, channelName);
//    }
//
//    public List<LedgerTaskTransaction> getTaskHistory(String key, User user, String organization,
//                                                      String channelName) {
//        return ledgerTaskService.getTaskHistory(key, user, organization, channelName);
//    }

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
