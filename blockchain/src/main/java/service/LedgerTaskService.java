//package service;
//
//import com.citynow.lightening.citynow.project.blockchain.client.ChannelWrapper;
//import com.citynow.lightening.citynow.project.blockchain.model.*;
//import com.citynow.lightening.citynow.project.blockchain.util.Util;
//import com.citynow.lightening.citynow.project.controller.api.Exception.ResourceInvalidException;
//import com.citynow.lightening.citynow.project.dto.UserAction;
//import com.citynow.lightening.citynow.project.model.User;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.hyperledger.fabric.sdk.BlockEvent;
//import org.hyperledger.fabric.sdk.ChaincodeResponse;
//import org.hyperledger.fabric.sdk.ProposalResponse;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//@Slf4j
//public class LedgerTaskService {
//
//    public static LedgerTaskService newInstance() {
//        return new LedgerTaskService();
//    }
//
//    public boolean saveTask(User user, LedgerTask ledgerTask, String organizationName, String channelName) throws Exception{
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            String actionCode;
//            log.info("ledgerTaskStatus " + ledgerTask.getStatus());
//            switch (ledgerTask.getStatus()) {
//                case "WAITING_FOR_ACCEPTANCE":
//                    actionCode = "REQUEST_TASK";
//                    break;
//                case "READY":
//                    actionCode = "ACCEPT_TASK";
//                    break;
//                case "NEW":
//                    actionCode = "DENY_TASK";
//                    break;
//                case "IN_PROGRESS":
//                    actionCode = "START_TASK";
//                    break;
//                case "PAUSED":
//                    actionCode = "PAUSE_TASK";
//                    break;
//                case "DONE":
//                    actionCode = "DONE_TASK";;
//                    break;
//                case "CLOSED":
//                    actionCode = "CLOSE_TASK";
//                    break;
//                default:
//                    throw new ResourceInvalidException("Invalid task status");
//            }
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsTask = objectMapper.writeValueAsString(ledgerTask);
//            UserAction userAction = new UserAction()
//                    .actionCode(actionCode)
//                    .userId(user.getId().toString())
//                    .asset("TASK");
//            String[] args = new String[]{userAction.toJSONString(),jsTask};
//            BlockEvent.TransactionEvent result = ChannelWrapper
//                    .getChannelWrapperInstance(
//                            user.getUsername(),
//                            organizationName)
//                    .invokeChainCode(
//                            channelName,
//                            organizationName,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "saveTask", args);
//            log.info("transaction is valid : " + result.isValid());
//            return result.isValid();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("Exception on saving task");
//        }
//    }
//
//    public List<LedgerTask> getTasks(User user, String organizationName, String channelName) {
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            UserAction userAction = new UserAction()
//                    .actionCode("GET_TASKS")
//                    .userId(user.getId().toString())
//                    .asset("TASK");
//            String[] args = {userAction.toJSONString()};
//            Collection<ProposalResponse> result = ChannelWrapper
//                    .getChannelWrapperInstance(
//                            user.getUsername(),
//                            organizationName)
//                    .queryChaincode2(
//                            channelName,
//                            organizationName,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "getTasks", args);
//            List<String> strings = new ArrayList<>();
//            result.forEach(proposalResponse -> strings.add(proposalResponse.getMessage()));
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(strings.get(0), new TypeReference<List<LedgerTask>>(){});
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public List<LedgerTaskTransaction> getTaskHistory(String key, User user, String organizationName,
//                                                      String channelName) {
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            UserAction userAction = new UserAction()
//                    .actionCode("GET_TASK_HISTORY")
//                    .userId(user.getId().toString())
//                    .asset("TASK");
//            String[] args = {userAction.toJSONString(), key};
//            Collection<ProposalResponse> result = ChannelWrapper
//                    .getChannelWrapperInstance(
//                            user.getUsername(),
//                            organizationName)
//                    .queryChaincode2(
//                            channelName,
//                            organizationName,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "getTaskHistory", args);
//
//            List<String> strings = new ArrayList<>();
//            result.forEach(proposalResponse -> strings.add(proposalResponse.getMessage()));
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(strings.get(0), new TypeReference<List<LedgerTaskTransaction>>(){});
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    public LedgerTaskPagination getTasksPagination(int page, int size, String direction,
//                                                   String sort, String search, String org, User user,
//                                                   String channelName) {
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            UserAction userAction = new UserAction()
//                    .actionCode("GET_TASK_PAGINATION")
//                    .userId(user.getId().toString())
//                    .asset("TASK");
//            String[] args = {userAction.toJSONString(), String.valueOf(page), String.valueOf(size)};
//            Collection<ProposalResponse> proposalResponses = ChannelWrapper
//                    .getChannelWrapperInstance(user.getUsername(), org)
//                    .queryChaincode2(channelName,
//                            org,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "getTasksPagination",
//                            args);
//            List<String> resultMessage = new ArrayList<>();
//            proposalResponses.forEach(proposalResponse -> {
//                if (proposalResponse.getStatus() == ChaincodeResponse.Status.SUCCESS)
//                    resultMessage.add(proposalResponse.getMessage());
//            });
//            LedgerTaskPagination participantJsonList =  new LedgerTaskPagination();
//            ObjectMapper mapper = new ObjectMapper();
//            if (resultMessage.size() == 0) {
//                participantJsonList.setTotalPages(0);
//                participantJsonList.setPage(1);
//                participantJsonList.setSize(size);
//                participantJsonList.setFirst(true);
//                participantJsonList.setLast(true);
//            } else {
//                participantJsonList = mapper.readValue(resultMessage.get(0), LedgerTaskPagination.class);
//                int pageCount = (int) Math.ceil(participantJsonList.getTotalCount()/(double) size);
//                participantJsonList.setTotalPages(pageCount);
//                participantJsonList.setPage(page+1);
//                participantJsonList.setSize(size);
//                participantJsonList.setFirst(page == 0);
//                participantJsonList.setLast(page + 1 == pageCount);
//            }
//
//            return participantJsonList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error on getting tasks");
//        }
//
//    }
//
//    public boolean saveRedmineTask(User user, LedgerRedmineTask ledgerRedmineTask, String organizationName, String channelName) throws Exception{
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            log.info("ledgerRedmineTaskStatus " + ledgerRedmineTask.getStatus());
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jsTask = objectMapper.writeValueAsString(ledgerRedmineTask);
//            UserAction userAction = new UserAction()
//                    .actionCode("SAVE_REDMINE_TASK")
//                    .userId(user.getId().toString())
//                    .asset("REDMINE_TASK");
//            String[] args = new String[]{userAction.toJSONString(),jsTask};
//            BlockEvent.TransactionEvent result = ChannelWrapper
//                    .getChannelWrapperInstance(
//                            user.getUsername(),
//                            organizationName)
//                    .invokeChainCode(
//                            channelName,
//                            organizationName,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "saveRedmineTask", args);
//            log.info("Transaction is valid : " + result.isValid());
//            return result.isValid();
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Exception("Exception on saving task");
//        }
//    }
//
//    public LedgerDefaultTaskPagination getAllTasksPagination(int page, int size, String direction,
//                                                             String sort, String search, String org, User user,
//                                                             String channelName, String typeTask) {
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            UserAction userAction = new UserAction()
//                    .actionCode("GET_All_TASK_PAGINATION")
//                    .userId(user.getId().toString())
//                    .asset("");
//            //Create sort value
//            StringBuilder sortString = new StringBuilder();
//            if(!sort.isEmpty()) {
//                sortString.append("{\"")
//                        .append(sort)
//                        .append("\":\"")
//                        .append(direction)
//                        .append("\"}");
//            }
//
//            String[] args = {userAction.toJSONString(), String.valueOf(page), String.valueOf(size), sortString.toString(), typeTask, search};
//            Collection<ProposalResponse> proposalResponses = ChannelWrapper
//                    .getChannelWrapperInstance(user.getUsername(), org)
//                    .queryChaincode2(channelName,
//                            org,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "getAllTasksPagination",
//                            args);
//            List<String> resultMessage = new ArrayList<>();
//            proposalResponses.forEach(proposalResponse -> {
//                if (proposalResponse.getStatus() == ChaincodeResponse.Status.SUCCESS)
//                    resultMessage.add(proposalResponse.getMessage());
//            });
//            LedgerDefaultTaskPagination participantJsonList =  new LedgerDefaultTaskPagination();
//            ObjectMapper mapper = new ObjectMapper();
//            if (resultMessage.size() == 0) {
//                participantJsonList.setTotalPages(0);
//                participantJsonList.setPage(1);
//                participantJsonList.setSize(size);
//                participantJsonList.setFirst(true);
//                participantJsonList.setLast(true);
//            } else {
//                participantJsonList = mapper.readValue(resultMessage.get(0), LedgerDefaultTaskPagination.class);
//                int pageCount = (int) Math.ceil(participantJsonList.getTotalCount()/(double) size);
//                participantJsonList.setTotalPages(pageCount);
//                participantJsonList.setPage(page+1);
//                participantJsonList.setSize(size);
//                participantJsonList.setFirst(page == 0);
//                participantJsonList.setLast(page + 1 == pageCount);
//            }
//
//            return participantJsonList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new RuntimeException("Error on getting tasks");
//        }
//    }
//
//    public List<LedgerDefaultTaskTransaction> getDefaultTaskHistory(String key, String type, User user, String organizationName,
//                                                                    String channelName) {
//        try {
//            UserContext userContext = Util.toUserContextFromHFUserContext(user.getHfUserContext());
//            UserAction userAction = new UserAction()
//                    .actionCode("GET_DEFAULT_TASK_HISTORY")
//                    .userId(user.getId().toString())
//                    .asset("");
//            String[] args = {userAction.toJSONString(), key, type};
//            Collection<ProposalResponse> result = ChannelWrapper
//                    .getChannelWrapperInstance(
//                            user.getUsername(),
//                            organizationName)
//                    .queryChaincode2(
//                            channelName,
//                            organizationName,
//                            userContext,
//                            Util.chaincodeFilter(channelName),
//                            "getDefaultTaskHistory", args);
//
//            List<String> strings = new ArrayList<>();
//            result.forEach(proposalResponse -> strings.add(proposalResponse.getMessage()));
//            ObjectMapper mapper = new ObjectMapper();
//            return mapper.readValue(strings.get(0), new TypeReference<List<LedgerDefaultTaskTransaction>>(){});
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//}
