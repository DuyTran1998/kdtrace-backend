package client;

import model.UserContext;
import network.FabricNetworkProfileLoader;
import org.hyperledger.fabric.sdk.*;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.concurrent.TimeUnit.SECONDS;

public class ChannelWrapper {
    private FabricClientWrapper fc;
    private String userName;
    private String org;

    /**
     * @param userName
     * @param org
     * @throws Exception
     */

    private ChannelWrapper(String userName, String org) {
        this.userName = userName;
        this.org = org;
        init();
    }

    /**
     * @param userName
     * @param org
     * @return
     */
    public static ChannelWrapper getChannelWrapperInstance(String userName, String org) {
        Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "getChannelWrapperInstance");
        return new ChannelWrapper(userName, org);
    }

    /**
     *
     */
    void init() {
        Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "init");
        this.fc = FabricClientWrapper.getFabricClient(userName, org);
    }

    /**
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return Collection<ProposalResponse>
     */
    public Collection<ProposalResponse> queryChaincode(String channelName, String chaincodeName, String fcn, String... args) {
        Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "queryChaincode");
        Collection<ProposalResponse> queryResponse = null;
        Channel channel = null;
        try {
            channel = fc.getChannelClient(channelName);
            //QueryByChaincodeRequest queryReq = QueryByChaincodeRequest.newInstance(CAClientWrapper.getUserContext(this.userName, this.org));
            User user = FabricNetworkProfileLoader.getNetworkConfig().getOrganizationInfo(this.org).getPeerAdmin();
            QueryByChaincodeRequest query = QueryByChaincodeRequest.newInstance(user);
            query.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
            query.setFcn(fcn);
            query.setArgs(args);
            query.setProposalWaitTime(20000);
            if (args != null) {
                query.setArgs(args);            }
            queryResponse = channel.queryByChaincode(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown(true);
            return queryResponse;
        }

    }

    /**
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return CompletableFuture<BlockEvent.TransactionEvent>
     */
//    public CompletableFuture<BlockEvent.TransactionEvent> invokeChainCode(String channelName, String
//    public List<String> invokeChainCode(String channelName, String
//            chaincodeName, String fcn, String[] args) {
//        Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "invokeChainCode");
//        for (String arg : args) {
//            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "String Arg: " + arg);
//        }
//        CompletableFuture<BlockEvent.TransactionEvent> commitResp = null;
//        Channel channel = null;
//        List<String> result = new ArrayList<>();
//        try {
//            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO,"Start invoke chain code:");
//
//            channel = fc.getChannelClient(channelName);
//            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO,"channel: " + channel.getName());
//            channel.getOrderers().forEach(item-> System.out.println(item.getName()));
//            channel.getPeers().stream().forEach(System.out::println);
//            User userContext = FabricNetworkProfileLoader.getNetworkConfig().getOrganizationInfo(this.org).getPeerAdmin();
//            TransactionProposalRequest transactionProposalRequest = TransactionProposalRequest.newInstance(userContext);
//            transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
//            transactionProposalRequest.setFcn(fcn);
//            transactionProposalRequest.setArgs(args);
//            transactionProposalRequest.setProposalWaitTime(10000);
//
//            Map<String, byte[]> tm = new HashMap<>();
//            tm.put("HyperLedgerFabric", "Java - SDK".getBytes(UTF_8));
//            tm.put("method", fcn.getBytes(UTF_8));
//            transactionProposalRequest.setTransientMap(tm);
//
//            Collection<ProposalResponse> response = channel.sendTransactionProposal(transactionProposalRequest);
//            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING,"Response.size:" + response.size());
//            List<ProposalResponse> proposalResponsesSuccess = new ArrayList<>();
//            for (ProposalResponse resp : response) {
//                ChaincodeResponse.Status status = resp.getStatus();
//                Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING, "Invoked chaincode:execute " + chaincodeName + " - " + fcn + ". Status - " + status);
//                Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING, "Response message " + resp.getMessage());
//                Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING, "Response " + resp.getProposal().getPayload().toString("UTF-8"));
//                //Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING, "payload" + resp.getProposalResponse().getP);
//                if (status.getStatus() == 200) {
//                    proposalResponsesSuccess.add(resp);
//                    result.add(resp.getMessage());
//                }
//                if (status.getStatus() != 200) {
//                    throw new Exception((resp.getMessage()));
//                }
//            }
//            //ChaincodeEndorsementPolicy chaincodeEndorsementPolicy =
//            Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(response);
//            if (proposalConsistencySets.size() != 1) {
//                throw new Exception("Expected only one set of consistent proposal responses but got more");
//            }
//            commitResp = channel.sendTransaction(proposalResponsesSuccess, userContext);
//            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "Invoked chaincode " + chaincodeName + " - " + fcn + ". Status - " + commitResp.toString());
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            channel.shutdown(true);
////            return commitResp;
//            return result;
//        }
//    }

    /**
     * @param txnId
     * @param channelName
     * @return TransactionInfo
     */
    public TransactionInfo queryByTransactionId(String txnId, String channelName) {
        Channel channel = null;
        TransactionInfo transactionInfo = null;
        try {
            channel = fc.getChannelClient(channelName);
            Collection<Peer> peers = channel.getPeers();
            for (Peer peer : peers) {
                transactionInfo = channel.queryTransactionByID(peer, txnId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.shutdown(true);
            return transactionInfo;
        }
    }

    /**
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return BlockEvent.TransactionEvent
     */
    public BlockEvent.TransactionEvent invokeChainCode(String channelName,
                                                       String org,
                                                       UserContext userContext,
                                                       String chaincodeName,
                                                       String fcn,
                                                       String[] args) {
        CompletableFuture<BlockEvent.TransactionEvent> commitResp = null;
        Channel channel = null;
        List<String> result = new ArrayList<>();
        try {
            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO,"Start invoke chain code:");
            channel = fc.getChannelClient(channelName);
            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO,"channel: " + channel.getName());
            channel.getOrderers().forEach(item-> System.out.println(item.getName()));
            channel.getPeers().stream().forEach(System.out::println);
//            UserContext userContext = Util.readUserContext(org, userName);
            TransactionProposalRequest transactionProposalRequest = TransactionProposalRequest.newInstance(userContext);
            transactionProposalRequest.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
            transactionProposalRequest.setFcn(fcn);
            transactionProposalRequest.setArgs(args);
            transactionProposalRequest.setProposalWaitTime(20000);
            Map<String, byte[]> tm = new HashMap<>();
            tm.put("HyperLedgerFabric", "Java - SDK".getBytes(UTF_8));
            tm.put("method", fcn.getBytes(UTF_8));
            transactionProposalRequest.setTransientMap(tm);

            Collection<ProposalResponse> response = channel.sendTransactionProposal(transactionProposalRequest);
            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING,"Response.size:" + response.size());
            List<ProposalResponse> proposalResponsesSuccess = new ArrayList<>();
            for (ProposalResponse resp : response) {
                ChaincodeResponse.Status status = resp.getStatus();
                Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING, "Invoked chaincode:execute " + chaincodeName + " - " + fcn + ". Status - " + status);
                Logger.getLogger(ChannelWrapper.class.getName()).log(Level.WARNING, "Response message " + resp.getMessage());
                if (status.getStatus() == 200) {
                    proposalResponsesSuccess.add(resp);
                    result.add(resp.getMessage());
                }
                if (status.getStatus() != 200) {
                    throw new Exception((resp.getMessage()));
                }
            }
            Collection<Set<ProposalResponse>> proposalConsistencySets = SDKUtils.getProposalConsistencySets(response);
            if (proposalConsistencySets.size() != 1) {
                throw new Exception("Expected only one set of consistent proposal responses but got more");
            }
            commitResp = channel.sendTransaction(proposalResponsesSuccess, userContext);
            Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "Invoked chaincode " + chaincodeName + " - " + fcn + ". Status - " + commitResp.toString());
            return commitResp.get(60, SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error! Invoke fail");
        } finally {
            channel.shutdown(true);
        }
    }

    /**
     * @param channelName
     * @param chaincodeName
     * @param fcn
     * @param args
     * @return Collection<ProposalResponse>
     */
    public Collection<ProposalResponse> queryChaincode2(String channelName, String organization, UserContext userContext, String chaincodeName, String fcn, String... args) {
        Logger.getLogger(ChannelWrapper.class.getName()).log(Level.INFO, "queryChaincode");
        Collection<ProposalResponse> queryResponse = null;
        Channel channel = null;
        try {
            channel = fc.getChannelClient(channelName);
            QueryByChaincodeRequest query = QueryByChaincodeRequest.newInstance(userContext);
//            User user = FabricNetworkProfileLoader.getNetworkConfig().getOrganizationInfo(this.org).getPeerAdmin();
//            QueryByChaincodeRequest query = QueryByChaincodeRequest.newInstance(user);
            query.setChaincodeID(ChaincodeID.newBuilder().setName(chaincodeName).build());
            query.setFcn(fcn);
            query.setArgs(args);
            query.setProposalWaitTime(20000);
            if (args != null) {
                query.setArgs(args);
            }
            queryResponse = channel.queryByChaincode(query);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //channel.shutdown(true);
            return queryResponse;
        }

    }


}
