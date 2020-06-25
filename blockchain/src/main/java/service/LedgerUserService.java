package service;

import client.ChannelWrapper;
import com.duytran.kdtrace.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.hyperledger.fabric.sdk.BlockEvent;
import util.Util;

@Slf4j
public class LedgerUserService {
    public static LedgerUserService newInstance() {
        return new LedgerUserService();
    }

    public boolean updateProducer(User user, LedgerProducer ledgerProducer, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsProducer = objectMapper.writeValueAsString(ledgerProducer);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateProducer",
                            new String[]{jsProducer});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving producer info");
        }
    }
    public boolean updateTransport(User user, LedgerTransport ledgerTransport, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsTransport = objectMapper.writeValueAsString(ledgerTransport);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateTransport",
                            new String[]{jsTransport});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving transport info");
        }
    }
    public boolean updateDeliveryTruck(User user, LedgerDeliveryTruck ledgerDeliveryTruck, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsDeliveryTruck = objectMapper.writeValueAsString(ledgerDeliveryTruck);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateDeliveryTruck",
                            new String[]{jsDeliveryTruck});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving DeliveryTruck info");
        }
    }

    public boolean updateDistributor(User user, LedgerDistributor ledgerDistributor, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsDistributor = objectMapper.writeValueAsString(ledgerDistributor);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateDistributor",
                            new String[]{jsDistributor});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving distributor info");
        }
    }
}
