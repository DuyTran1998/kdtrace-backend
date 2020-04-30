package client;

import model.UserContext;
import network.FabricNetworkProfileLoader;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric_ca.sdk.Attribute;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.HFCAIdentity;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.AffiliationException;
import org.hyperledger.fabric_ca.sdk.exception.InvalidArgumentException;
import util.Util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CAClientWrapper {
    private HFCAClient hfcaClient;
    private String org;

    public static CAClientWrapper newInstance(String organization) {
        return new CAClientWrapper(organization);
    }

    public CAClientWrapper(String org) {
        this.org = org;
        init();
    }

    /**
     * Return UserContextMapper for user from store /cred directory.
     *
     * @param userName
     * @return UserContextMapper, null if not found
     * @throws Exception
     */
    public static UserContext getUserContext(String userName, String org) {
        UserContext userContext;
        userContext = Util.readUserContext(org, userName);
        if (userContext != null) {
            return userContext;
        }
        Logger.getLogger(CAClientWrapper.class.getName()).log(Level.SEVERE, "Userconext not found in store for " + userName + ". Enroll the user.");
        return userContext;

    }

    void init() {
        try {
            this.hfcaClient = HFCAClient.createNewInstance(FabricNetworkProfileLoader.getCaInfo(org));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Enroll the admin. This admin will be used as a registrar to register other users.
     *
     * @param name   - admin name
     * @param secret - admin secret
     * @return adminContext
     * @throws Exception
     */

    public UserContext enrollAdmin(String name, String secret, String org) throws Exception {
        UserContext adminContext;

        Enrollment enrollment = hfcaClient.enroll(name, secret);
        Logger.getLogger(CAClientWrapper.class.getName()).log(Level.INFO, "Admin enrolled.");

        adminContext = new UserContext();
        adminContext.setName(name);
        adminContext.setEnrollment(enrollment);
        adminContext.setAffiliation(FabricNetworkProfileLoader.getOrgInfo(org).getName());
        adminContext.setMspId(FabricNetworkProfileLoader.getOrgInfo(org).getMspId());

        Util.writeUserContext(adminContext);
        return adminContext;
    }

    /**
     * Register and enroll the user with organization MSP provider. User context saved in  /cred directory.
     * This is an admin function; admin should be enrolled before enrolling a user.
     *
     * @param registrarAdmin - network admin
     * @return UserContextMapper
     * @throws Exception
     */
    public UserContext registerUser(UserContext registrarAdmin, String organization, com.duytran.kdtrace.entity.User user) throws Exception {
        UserContext userContext;

        RegistrationRequest regRequest = new RegistrationRequest(new Long(user.getId()).toString() + "-" + user.getUsername(), organization);
        regRequest.setType("user");
        Map<String,Set<String>> maps;
        regRequest.addAttribute(new Attribute("id", new Long(user.getId()).toString(), true));
        regRequest.addAttribute(new Attribute("userName", user.getUsername(), true));
        regRequest.addAttribute(new Attribute("systemRole", user.getRole().getRoleName().name(), true));
        regRequest.addAttribute(new Attribute("organization", user.getOrganization(), true));

        if (registrarAdmin == null) {
            Logger.getLogger(CAClientWrapper.class.getName()).log(Level.SEVERE, "Registrar admin is not enrolled. Enroll Registrar.");
            throw new Exception("registrar context not valid");
        }
        String enrollSecret = hfcaClient.register(regRequest, registrarAdmin);
        Enrollment enrollment = hfcaClient.enroll(new Long(user.getId()).toString() + "-" + user.getUsername(), enrollSecret);

        userContext = new UserContext();
        userContext.setMspId(FabricNetworkProfileLoader.getOrgInfo(org).getMspId());
        userContext.setAffiliation(org);
        userContext.setEnrollment(enrollment);
        userContext.setName(new Long(user.getId()).toString() + "-" + user.getUsername());
        Logger.getLogger(CAClientWrapper.class.getName()).log(Level.INFO, "UserName - " + user.getUsername() + "  is successfully registered and enrolled by registrar -  " + registrarAdmin);
        return userContext;
    }

    /**
     * Usercontext is  generated from user secret key and store is also refreshed,
     * * User must be registered with MSP provider.
     *
     * @param userName
     * @param enrollSecret
     * @return UserContextMapper
     * @throws Exception
     */
    public UserContext getUserContext(String userName, String enrollSecret, String org) throws Exception {

        Enrollment enrollment = hfcaClient.enroll(userName, enrollSecret);

        UserContext userContext = new UserContext();
        userContext.setMspId(FabricNetworkProfileLoader.getOrgInfo(org).getMspId());
        userContext.setAffiliation(this.org);
        userContext.setEnrollment(enrollment);
        userContext.setName(userName);

        Util.writeUserContext(userContext);
        Logger.getLogger(CAClientWrapper.class.getName()).log(Level.INFO, "UserName - " + userName + "  is successfully enrolled ");
        return userContext;
    }

    public void newAffiliation(String org, UserContext registrar)  {
        try {
            hfcaClient.newHFCAAffiliation(org).create(registrar);
        } catch (InvalidArgumentException e) {
            e.printStackTrace();
        }
        catch (AffiliationException ignored) {
        }
    }

//    public UserContext updateIdentity(User registrar, com.duytran.kdtrace.entity.User user) {
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            if (userContext == null)
//                throw new ResourceNotFoundException("Not found valid user context");
//            HFCAIdentity newIdentity = hfcaClient.newHFCAIdentity(user.getId().toString() + "-" + user.getUsername());
//            Map<String,Set<String>> maps;
//            maps = Util.getPrivilegeFromList(user.getUserProjectRoleList());
//            String projectPriv = Util.convertToProjectPrivilegesString(maps);
//            Logger.getLogger(CAClientWrapper.class.getName()).log(Level.SEVERE, "ProjectPrivileges: " + projectPriv);
//            Set<Attribute> attributes = new HashSet<>();
//            attributes.add(new Attribute("rc.id", new Long(user.getId()).toString(), true));
//            attributes.add(new Attribute("rc.userName", user.getUsername(), true));
//            attributes.add(new Attribute("rc.systemRole", user.getRole().getCode(), true));
//            attributes.add(new Attribute("rc.projectPrivilege", projectPriv, true));
//            attributes.add(new Attribute("rc.organization", user.getUserBranchOrganizations().get(0).getOrganization().getCode(), true));
//            newIdentity.setAttributes(attributes);
//            int updateIdentity = newIdentity.update(registrar);
//            if (updateIdentity==200) {
//                Enrollment newEnrollment = hfcaClient.reenroll(userContext);
//                userContext.setEnrollment(newEnrollment);
//            }
//            return userContext;
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
