package service;

import client.ChannelWrapper;
import com.duytran.kdtrace.entity.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import model.LedgerProducer;
import model.UserContext;
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
            throw new Exception("Exception on saving task");
        }
    }

}
