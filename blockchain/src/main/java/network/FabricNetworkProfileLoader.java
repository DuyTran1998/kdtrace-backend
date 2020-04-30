package network;

import lombok.extern.slf4j.Slf4j;
import org.hyperledger.fabric.sdk.NetworkConfig;

import java.io.File;

@Slf4j
public class FabricNetworkProfileLoader {
    private static final Integer lock = 0;
    private static FabricNetworkProfileLoader fabricNetworkProfileLoader = null;
    private static NetworkConfig networkConfig;

    private FabricNetworkProfileLoader() throws Exception {
        //networkConfig = NetworkConfig.fromJsonFile((new File("./networkConfig.json")));
        //fabricConfig = new FabricConfiguration.config();
        this.networkConfig = NetworkConfig.fromYamlFile(new File("./blockchain/connection-kdtrace.yaml"));
    }

    /**
     * Get Certificate Authority networkConfig for an organization
     *
     * @param org
     * @return CAInfo
     * @throws Exception
     */
    public static NetworkConfig.CAInfo getCaInfo(String org) throws Exception {
        if (networkConfig == null) {
            getInstance();
        }
        //assuming there is only one ca per organisation
        return networkConfig.getOrganizationInfo(org).getCertificateAuthorities().get(0);
    }

    /**
     * Get organization networkConfig
     *
     * @param org
     * @return OrgInfo
     * @throws Exception
     */
    public static NetworkConfig.OrgInfo getOrgInfo(String org) throws Exception {
        if (networkConfig == null) {
            getInstance();
        }
        return networkConfig.getOrganizationInfo(org);
    }

    /**
     * Return class instance
     *
     * @return LoadConnectionProfile
     * @throws Exception
     */
    public static FabricNetworkProfileLoader getInstance() throws Exception {

        synchronized (lock) {
            if (fabricNetworkProfileLoader == null) {
                fabricNetworkProfileLoader = new FabricNetworkProfileLoader();
            }
        }
        return fabricNetworkProfileLoader;
    }

    /**
     * Return the complete configuartion info
     *
     * @return NetworkConfig
     * @throws Exception
     */
    public static NetworkConfig getNetworkConfig() throws Exception {
        if (networkConfig == null) {
            getInstance();
        }
        return networkConfig;
    }

    public NetworkConfig config() {
        return networkConfig;
    }


}

