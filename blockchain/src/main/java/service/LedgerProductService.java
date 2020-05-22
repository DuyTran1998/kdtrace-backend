package service;

import client.ChannelWrapper;
import com.duytran.kdtrace.entity.DeliveryTruck;
import com.duytran.kdtrace.entity.StatusQRCode;
import com.duytran.kdtrace.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.*;
import org.hyperledger.fabric.sdk.BlockEvent;
import util.Util;

import java.util.List;
import java.util.Map;

@Slf4j
public class LedgerProductService {
    public static LedgerProductService newInstance() {
        return new LedgerProductService();
    }

    public boolean updateProduct(User user, LedgerProduct ledgerProduct, String organizationName, String channelName) throws Exception {
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

    public boolean createQRCodes(User user, List<LedgerQRCode> qrCodeList, String organizationName, String channelName) throws Exception {
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
                            "createQRCodes",
                            new String[]{jsQRCodes});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving QRCode list");
        }
    }

    public boolean saveQRCodes(User user, List<Long> qrCodeIds, String statusQRCode, Map<Long, String> mapOtp, String organizationName, String channelName) throws Exception {
        try {
            String checkQrCodeIds = "";
            String checkmapOtp = "";
            if (qrCodeIds != null)
                checkQrCodeIds = qrCodeIds.toString();
            if (mapOtp != null){
                ObjectMapper objectMapper = new ObjectMapper();
                checkmapOtp = objectMapper.writeValueAsString(mapOtp);
            }
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
                            "saveQRCodes",
                            new String[]{checkQrCodeIds, '"' + statusQRCode + '"', checkmapOtp});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving QRCode list");
        }
    }

    public boolean createProcess(User user, LedgerProcess ledgerProcess, String organizationName, String channelName) throws Exception {
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

    public boolean updateProcess(User user, Long processId, String statusProcess,
                                 List<Long> qrCodes, Long transportId, Long deliveryTruckId, String delivery_at, String receipt_at, String organizationName, String channelName) throws Exception {
        try {
            String checkQrCodes = "";
            String checkTransportId = "";
            String checkDeliveryTruckId = "";
            String checkDelivery_at = "";
            String checkReceipt_at = "";
            if (qrCodes != null)
                checkQrCodes = qrCodes.toString();
            if (transportId != null)
                checkTransportId = transportId.toString();
            if (deliveryTruckId != null)
                checkDeliveryTruckId = deliveryTruckId.toString();
            if (delivery_at != null)
                checkDelivery_at = delivery_at;
            if (receipt_at != null)
                checkReceipt_at = receipt_at;
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
                            new String[]{processId.toString(), '"' + statusProcess + '"',
                                    checkQrCodes, checkTransportId, checkDeliveryTruckId, '"' + checkDelivery_at + '"', '"' + checkReceipt_at + '"'});
            log.info("transaction is valid : " + result.isValid());
            return result.isValid();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Exception on saving Process");
        }
    }
}
