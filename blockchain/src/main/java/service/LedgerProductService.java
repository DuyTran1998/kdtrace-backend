package service;

import client.ChannelWrapper;
import com.duytran.kdtrace.entity.DeliveryTruck;
import com.duytran.kdtrace.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.hyperledger.fabric.sdk.BlockEvent;
import util.Util;

import java.util.List;

@Slf4j
public class LedgerProductService {
    public static LedgerProductService newInstance() {
        return new LedgerProductService();
    }

    public boolean updateProduct(User user, LedgerProduct ledgerProduct, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsProduct = objectMapper.writeValueAsString(ledgerProduct);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateProduct",
                            new String[]{jsProduct});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving product info");
        }
    }

    public boolean updateQRCodes(User user, List<LedgerQRCode> qrCodeList, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsQRCodes = objectMapper.writeValueAsString(qrCodeList);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateQRCodes",
                            new String[]{jsQRCodes});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving QRCode list");
        }
    }

    public boolean createProcess(User user, LedgerProcess ledgerProcess, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            ObjectMapper objectMapper = new ObjectMapper();
            String jsProcess = objectMapper.writeValueAsString(ledgerProcess);
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "createProcess",
                            new String[]{jsProcess});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving Process");
        }
    }

    public boolean updateProcess(User user, Long processId, String statusProcess,  List<Long> qrCodes, String organizationName, String channelName) throws Exception{
        try {
            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
            BlockEvent.TransactionEvent result = ChannelWrapper
                    .getChannelWrapperInstance(
                            user.getUsername(),
                            organizationName)
                    .invokeChainCode(
                            channelName,
                            organizationName,
                            userContext,
                            "process_" + channelName,
                            "updateProcess",
                            new String[]{processId.toString(), statusProcess, String.valueOf(qrCodes)});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving Process");
        }
    }
}
