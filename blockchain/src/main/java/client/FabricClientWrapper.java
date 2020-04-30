package client;

import network.FabricNetworkProfileLoader;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

import java.util.logging.Level;
import java.util.logging.Logger;

//@Slf4j
public class FabricClientWrapper {

    private String org;
    private String userName;
    private HFClient hfClient;

    private FabricClientWrapper(String userName, String org) {
        this.org = org;
        this.userName = userName;
        init();
    }

    /**
     * Return instance of FabricClientWrapper
     *
     * @param userName
     * @param org
     * @return FabricClientWrapper
     * @throws Exception
     */
    public static FabricClientWrapper getFabricClient(String userName, String org) {
        Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO, "getFabricClient : new FabricClientWrapper");
        return new FabricClientWrapper(userName, org);
    }

    /**
     *
     */
    void init() {
        try {
            this.hfClient = HFClient.createNewInstance();
            CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
            this.hfClient.setCryptoSuite(cryptoSuite);
//            UserContextMapper userContext = CAClientWrapper.getUserContext(userName, org);
            User user = FabricNetworkProfileLoader.getNetworkConfig().getOrganizationInfo(this.org).getPeerAdmin();
//            Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO, "getUserAdmin: " + user.getName());
//            UserContextMapper adminUserContext = new UserContextMapper();
//            adminUserContext.setName(userName);
//            adminUserContext.setAffiliation(org);
//            adminUserContext.setMspId("OrgCitynowMSP");
            this.hfClient.setUserContext(user);
            Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO, "after setUserContext: " + this.hfClient.getUserContext().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return an instance of Channel. The channel client provide various transaction functions
     *
     * @param channelName
     * @return Channel
     * @throws Exception
     */
    public Channel getChannelClient(String channelName) {
        Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO,"getChannelClient");
        Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO,"getChannelClient: channelName=" + channelName);
        Channel channel = null;
        try {

            channel = hfClient.loadChannelFromConfig(channelName, FabricNetworkProfileLoader.getNetworkConfig());
            Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO,"channel.getName: " + channel.getName());
            channel = channel.initialize();
            Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO,"channelInitialize alrealdy: getName=" + channel.getName());
        } catch (Exception e ) {
            Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO, "Getting Channel Client Error");
            e.printStackTrace();
        }
        Logger.getLogger(FabricClientWrapper.class.getName()).log(Level.INFO, "channel");
        return channel;
    }

    /**
     * Return HFClient object
     *
     * @return HFClient
     */
    public HFClient getHfClient() {
        return hfClient;
    }
}
