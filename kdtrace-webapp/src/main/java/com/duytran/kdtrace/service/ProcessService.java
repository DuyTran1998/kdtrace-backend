package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.*;
import com.duytran.kdtrace.model.EndUserResponse;
import com.duytran.kdtrace.model.ProcessModel;
import com.duytran.kdtrace.model.RequestProcessModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.ProcessRepository;
import com.duytran.kdtrace.repository.QRCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ProcessService {
    @Autowired
    private ProcessRepository processRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private DistributorService distributorService;

    @Autowired
    private QRCodeRepository qrCodeRepository;

    @Autowired
    private TransportService transportService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private EmailService emailService;


    @Transactional
    public ResponseModel createProcess(RequestProcessModel processModel){
        if(productService.checkQuanlityProducts(processModel.getId_product(), processModel.getQuantity())){
            Distributor distributor = distributorService.getDistributorInPrincipal();
            Process process = new Process(distributor,
                                          processModel.getId_product(),
                                          processModel.getQuantity(),
                                          StatusProcess.WAITING_RESPONSE_PRODUCER);

            Process savedProcess = processRepository.save(process);

            sendEmailToProducer(processModel.getId_product(),
                                processModel.getQuantity(),
                                savedProcess.getId(),
                                distributor.getCompanyName(),
                                distributor.getPhone());

            productService.changeStatusQRCode(processModel.getId_product(),
                                              processModel.getQuantity(),
                                              StatusQRCode.WAITING);

            return new ResponseModel("Sucessfull", HttpStatus.OK.value(), processModel);
        }
        return new ResponseModel("Don't enough quanlity product to buy", HttpStatus.BAD_REQUEST.value(), processModel);
    }

    private void sendEmailToProducer(Long productID, Long quanlity, Long processID, String companyName, String phone){
        String email = producerService.getMail(productID);
        String link = "http://localhost:8080/api/process/get?id=" + processID;
        String content = "Notification from " + companyName + "\n" +
                         "We want to by " + quanlity.toString() + " products have Id: " + productID.toString() + "\n" +
                         "link: " + link +  "\n" +
                         "Please contact with us " + phone;
        String subject = "Offer to buy product";
        emailService.sendEmail(subject,content,email);
    }

    public ResponseModel getProcess(Long id){
        Process process = findProcessById(id);
        ProcessModel processModel = ProcessMapper.INSTANCE.processToProcessModel(process);
        processModel.updateProcessModel(DistributorMapper.INSTANCE.distributorToDistributorModel(process.getDistributor()),
                                        DeliveryTruckMapper.INSTANCE.deliveryTruckToDeliveryTruckModel(process.getDeliveryTruck()),
                                        ProductMapper.INSTANCE.qrCodeListToQRCodeModel(process.getQrCodes()));
        return new ResponseModel("Process", HttpStatus.OK.value(), processModel);
    }

    public ResponseModel acceptToSell(Long id){
        Process process = findProcessById(id);
        Producer producer = producerService.getProducerInPrincipal();

        if(!productService.checkExistProductByIdAndProducer(process.getProductID(), producer.getId()) ||
                process.getStatusProcess() != StatusProcess.WAITING_RESPONSE_PRODUCER){
            return new ResponseModel("You don't have permission", HttpStatus.FORBIDDEN.value(), id);
        }

        process.setStatusProcess(StatusProcess.CHOOSE_DELIVERYTRUCK_TRANSPORT);
        List<QRCode> qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(process.getProductID(), "WAITING");
        qrCodes.subList(0, (int) process.getQuanlity() -1 ).forEach(
                i -> {
                    i.setProcess(process);
                    qrCodeRepository.save(i);
                }
        );
        Process savedProcess = processRepository.save(process);
        sendEmailResponseToDistributor(savedProcess, producer);
        return new ResponseModel("Accepted", HttpStatus.OK.value(), id);
    }

    private void sendEmailResponseToDistributor(Process process, Producer producer){
        String link = "http://localhost:8080/api/process/get?id=" + process.getId();
        String listCode = reverseListQRCodeToString(process.getQrCodes());
        String content = "Notification from " + producer.getCompanyName() + "\n" +
                "We accepted to sell " + process.getQuanlity() + " products have Id: " + process.getProductID().toString() + "\n" +
                "List Code:" +"\n" +
                listCode +
                "link: " + link +  "\n" +
                "Please contact with us " + producer.getPhone();
        emailService.sendEmail("Response agreement with " + process.getDistributor().getCompanyName(),
                                content, process.getDistributor().getEmail());
    }

    private String reverseListQRCodeToString(List<QRCode> qrCodes){
        String result = "";
        for(QRCode qrCode : qrCodes){
            result = result.concat(qrCode.getCode() + "\n");
        }
        return result;
    }

//    public ResponseModel chooseTransport(Long id){
//
//    }

    public ResponseModel acceptToDelivery(Long id, Long id_deliveryTruck){
        Process process = findProcessById(id);
        DeliveryTruck deliveryTruck = transportService.findDeliveryTruckById(id_deliveryTruck);
        process.setDeliveryTruck(deliveryTruck);
        process.setStatusProcess(StatusProcess.ON_BOARDING_GET);
        processRepository.save(process);
        return new ResponseModel("Accepted", HttpStatus.OK.value(), id);
    }

    public ResponseModel confirmToGetGoods(Long id){
        Process process = findProcessById(id);
        process.setDelivery_at(commonService.getDateTime());
        process.setStatusProcess(StatusProcess.ON_BOARDING_REVEIVE);
        processRepository.save(process);
        return new ResponseModel("The Goods was taken by transport", HttpStatus.OK.value(), id);
    }

    public ResponseModel confirmToReceiptGoods(Long id){
        Process process = findProcessById(id);
        process.setReceipt_at(commonService.getDateTime());
        process.setStatusProcess(StatusProcess.REVEIVED);
        processRepository.save(process);
        List<QRCode> qrCodes = qrCodeRepository.findAllByProcess_Id(id);
        qrCodes.forEach(
        i -> {
            i.setStatusQRCode(StatusQRCode.READY);
            qrCodeRepository.save(i);
        }
        );
        return new ResponseModel("The Goods was receipted by distributor", HttpStatus.OK.value(), id);
    }

    private Process findProcessById(Long id){
        Process process = processRepository.findProcessById(id).orElseThrow(
                () -> new RecordNotFoundException("Not Found")
        );
        return process;
    }

    public List<QRCode> getQRCodeByProcessId(Long id){
        List<QRCode> qrCodes = qrCodeRepository.findAllByProcess_Id(id);
        return qrCodes;
    }

    public ResponseModel getInfomation(String code){
        QRCode qrCode = qrCodeRepository.findByCode(code).orElseThrow(
                ()-> new RecordNotFoundException("Don't found QRCode with code")
        );
        Process process = qrCode.getProcess();
        EndUserResponse response = new EndUserResponse();
        Product product = productService.getProductEntityById(process.getProductID());
        product.setCodes(null);
        DeliveryTruck deliveryTruck = transportService.findDeliveryTruckById(process.getDeliveryTruck().getId());
        response.setProductModel(ProductMapper.INSTANCE.productToProductModel(product));
        response.setProducerModer(ProducerMapper.INSTANCE.producerToProducerModel(product.getProducer()));
        response.setDeliveryTruckModel(DeliveryTruckMapper.INSTANCE.deliveryTruckToDeliveryTruckModel(deliveryTruck));
        response.setTransportModel(TransportMapper.INSTANCE.transportToTransportModel(deliveryTruck.getTransport()));
        response.setDistributorModel(DistributorMapper.INSTANCE.distributorToDistributorModel(process.getDistributor()));
        response.setTime1(process.getDelivery_at());
        response.setTime2(process.getReceipt_at());
        return new ResponseModel("Successfully", HttpStatus.OK.value(), response);
    }
}