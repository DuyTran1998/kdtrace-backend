package service;


import client.CAClientWrapper;
import com.duytran.kdtrace.entity.User;
import lombok.extern.slf4j.Slf4j;
import model.UserContext;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class CAIdentityService {

    @Value("${hyperledger.fabric.ca.admin.name}")
    private String adminName;
    @Value("${hyperledger.fabric.ca.admin.adminPassword}")
    private String adminPassword;

    public static CAIdentityService newInstance() {
        return new CAIdentityService();
    }

    public UserContext enrollAdmin(String organization) throws Exception {
        return new CAClientWrapper(organization).enrollAdmin("admin", "adminpw", organization);
    }

    public UserContext registerIdentity(String organization, User user, UserContext adminContext) {
        CAClientWrapper caClientWrapper = CAClientWrapper.newInstance(organization);
        caClientWrapper.newAffiliation(organization, adminContext);
        try {
            return caClientWrapper.registerUser(adminContext, organization, user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
