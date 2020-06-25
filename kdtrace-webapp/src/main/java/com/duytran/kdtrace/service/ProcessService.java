package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.*;
import com.duytran.kdtrace.model.EndUserResponse;
import com.duytran.kdtrace.model.ProcessModel;
import com.duytran.kdtrace.model.RequestProcessModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.DeliveryTruckRepository;
import com.duytran.kdtrace.repository.ProcessRepository;
import com.duytran.kdtrace.repository.QRCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

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

    @Autowired
    private BlockchainService blockchainService;
    @Autowired
    private DeliveryTruckRepository deliveryTruckRepository;


    @Transactional
    public ResponseModel createProcess(RequestProcessModel processModel) {
        if (productService.checkQuanlityProducts(processModel.getId_product(), processModel.getQuantity())) {
            Distributor distributor = distributorService.getDistributorInPrincipal();
            Process process = new Process(distributor,
                    processModel.getId_product(),
                    processModel.getQuantity(),
                    StatusProcess.WAITING_RESPONSE_PRODUCER);

            Process savedProcess = processRepository.save(process);
            blockchainService.createProcess(distributor.getUser(), process, "kdtrace");
            sendEmailToProducer(processModel.getId_product(),
                    processModel.getQuantity(),
                    savedProcess.getId(),
                    distributor.getCompanyName(),
                    distributor.getPhone());

            productService.changeStatusQRCode(distributor.getUser(),
                    processModel.getId_product(),
                    processModel.getQuantity(),
                    StatusQRCode.WAITING);

            return new ResponseModel("Sucessfull", HttpStatus.OK.value(), processModel);
        }
        return new ResponseModel("Don't enough quanlity product to buy", HttpStatus.BAD_REQUEST.value(), processModel);
    }

    private void sendEmailToProducer(Long productID, Long quanlity, Long processID, String companyName, String phone) {
        String email = producerService.getMail(productID);
        String link = "http://localhost:8080/api/process/get?id=" + processID;
        String content = "Notification from " + companyName + "\n" +
                "We want to by " + quanlity.toString() + " products have Id: " + productID.toString() + "\n" +
                "link: " + link + "\n" +
                "Please contact with us " + phone;
        String subject = "Offer to buy product";
        emailService.sendEmail(subject, content, email);
    }

    public ResponseModel getProcess(Long id) {
        Process process = findProcessById(id);
        ProcessModel processModel = ProcessMapper.INSTANCE.processToProcessModel(process);
        processModel.updateProcessModel(DistributorMapper.INSTANCE.distributorToDistributorModel(process.getDistributor()),
                DeliveryTruckMapper.INSTANCE.deliveryTruckToDeliveryTruckModel(process.getDeliveryTruck()),
                ProductMapper.INSTANCE.qrCodeListToQRCodeModel(process.getQrCodes()));
        return new ResponseModel("Process", HttpStatus.OK.value(), processModel);
    }

    @Transactional
    public ResponseModel acceptToSell(Long id) {
        Process process = findProcessById(id);
        Producer producer = producerService.getProducerInPrincipal();

        if (!productService.checkExistProductByIdAndProducer(process.getProductID(), producer.getId()) ||
                process.getStatusProcess() != StatusProcess.WAITING_RESPONSE_PRODUCER) {
            return new ResponseModel("You don't have permission", HttpStatus.METHOD_NOT_ALLOWED.value(), id);
        }
        process.setStatusProcess(StatusProcess.CHOOSE_DELIVERYTRUCK_TRANSPORT);
        List<QRCode> qrCodes = qrCodeRepository.getListQRCodeByProductIdAndStatusQRCode(process.getProductID(), "WAITING");
        List<Long> listQrCodeId = new ArrayList<>();
        qrCodes.subList(0, (int) process.getQuanlity() - 1).forEach(
                i -> {
                    i.setProcess(process);
                    qrCodeRepository.save(i);
                    listQrCodeId.add(i.getId());
                }
        );
        Process savedProcess = processRepository.save(process);
        blockchainService.updateProcess(producer.getUser(), process.getId(), StatusProcess.CHOOSE_DELIVERYTRUCK_TRANSPORT,
                listQrCodeId, null, null, null, null, producer.getOrgMsp(), "kdtrace");
        sendEmailResponseToDistributor(savedProcess, producer);
        return new ResponseModel("Accepted", HttpStatus.OK.value(), id);
    }

    private void sendEmailResponseToDistributor(Process process, Producer producer) {
        String link = "http://localhost:8080/api/process/get?id=" + process.getId();
        String listCode = reverseListQRCodeToString(process.getQrCodes());
        String content = "Notification from " + producer.getCompanyName() + "\n" +
                "We accepted to sell " + process.getQuanlity() + " products have Id: " + process.getProductID().toString() + "\n" +
                "List Code:" + "\n" +
                listCode +
                "link: " + link + "\n" +
                "Please contact with us " + producer.getPhone();
        emailService.sendEmail("Response agreement with " + process.getDistributor().getCompanyName(),
                content, process.getDistributor().getEmail());
    }

    private String reverseListQRCodeToString(List<QRCode> qrCodes) {
        String result = "";
        for (QRCode qrCode : qrCodes) {
            result = result.concat(qrCode.getCode() + "\n");
        }
        return result;
    }

    @Transactional
    public ResponseModel chooseTransport(Long id, Long transport_id) {
        Process process = findProcessById(id);
        Distributor distributor = distributorService.getDistributorInPrincipal();
        if (!distributor.getCompanyName().equals(process.getDistributor().getCompanyName()) ||
                process.getStatusProcess() != StatusProcess.CHOOSE_DELIVERYTRUCK_TRANSPORT) {
            return new ResponseModel("You don't have permission", HttpStatus.METHOD_NOT_ALLOWED.value(), id);
        }
        process.setTransportID(transport_id);
        process.setStatusProcess(StatusProcess.WAITING_RESPONSE_TRANSPORT);
        blockchainService.updateProcess(distributor.getUser(), process.getId(), StatusProcess.WAITING_RESPONSE_TRANSPORT,
                null, transport_id, null, null, null, distributor.getOrgMsp(), "kdtrace");
        Process savedProcess = processRepository.save(process);
        sendEmailToTransport(savedProcess);
        return new ResponseModel("Choose Transport Successfully", HttpStatus.OK.value(), id);
    }

    private void sendEmailToTransport(Process process) {
        String link = "http://localhost:8080/api/process/get?id=" + process.getId();
        String transport_email = transportService.getEmailByTransportId(process.getTransportID());
        String address = producerService.getAddress(process.getProductID());
        String content = "Notification from " + process.getDistributor().getCompanyName() + "\n" +
                "We want to express " + process.getQuanlity() + " products have Id: " + process.getProductID().toString() + "\n" +
                "At: " + address +
                "link: " + link + "\n" +
                "Please contact with us " + process.getDistributor().getPhone();
        emailService.sendEmail("Deal with Transport Company to express goods", content, transport_email);
    }

    @Transactional
    public ResponseModel   acceptToDelivery(Long id, Long id_deliveryTruck) {
        Process process = findProcessById(id);
        Transport transport = transportService.getTransportInPrincipal();
        if (!transport.getId().equals(process.getTransportID()) ||
                process.getStatusProcess() != StatusProcess.WAITING_RESPONSE_TRANSPORT) {
            return new ResponseModel("You don't have permission", HttpStatus.METHOD_NOT_ALLOWED.value(), id);
        }
        DeliveryTruck deliveryTruck = transportService.findDeliveryTruckById(id_deliveryTruck);
        deliveryTruck.setStatus(Status.ON_DELIVERY);
        deliveryTruckRepository.save(deliveryTruck);
        process.setDeliveryTruck(deliveryTruck);
        process.setStatusProcess(StatusProcess.ON_BOARDING_GET);
        Process savedProcess = processRepository.save(process);
        blockchainService.updateDeliveryTruck(transport.getUser(), deliveryTruck, transport.getOrgMsp(), "kdtrace");
        blockchainService.updateProcess(transport.getUser(), process.getId(), StatusProcess.ON_BOARDING_GET,
                null, null, deliveryTruck.getId(), null, null, transport.getOrgMsp(), "kdtrace");
        sendEmailToProducerAndDistributor(savedProcess, transport.getCompanyName(), transport.getPhone(),
                "We confirm to express",
                "Nofication from " + transport.getCompanyName());
        return new ResponseModel("Accepted", HttpStatus.OK.value(), id);
    }

    private void sendEmailToProducerAndDistributor(Process process, String nameCompany, String phone, String topic, String subject) {
        String link = "http://localhost:8080/api/process/get?id=" + process.getId();
        String emailProducer = producerService.getMail(process.getProductID());
        String content = "Notification from " + nameCompany + "\n" +
                topic + " " + process.getQuanlity() + " products have Id: " + process.getProductID().toString() + "\n" +
                "Information Delivery Truck: " + "\n" +
                "Plate: " + process.getDeliveryTruck().getNumberPlate() + "\n" +
                "Type: " + process.getDeliveryTruck().getAutoMaker() + "\n" +
                "link: " + link + "\n" +
                "Please contact with us " + phone;
        emailService.sendEmail(subject, content, emailProducer);
        emailService.sendEmail(subject, content, process.getDistributor().getEmail());
    }

    @Transactional
    public ResponseModel confirmToGetGoods(Long id) {
        Process process = findProcessById(id);
        Transport transport = transportService.getTransportInPrincipal();
        if (!transport.getId().equals(process.getTransportID()) ||
                process.getStatusProcess() != StatusProcess.ON_BOARDING_GET) {
            return new ResponseModel("You don't have permission", HttpStatus.METHOD_NOT_ALLOWED.value(), id);
        }
        process.setDelivery_at(commonService.getDateTime());
        process.setStatusProcess(StatusProcess.ON_BOARDING_REVEIVE);
        Process savedProcess = processRepository.save(process);
        blockchainService.updateProcess(transport.getUser(), process.getId(), StatusProcess.ON_BOARDING_REVEIVE,
                null, null, null, process.getDelivery_at(), null, transport.getOrgMsp(), "kdtrace");
        sendEmailToProducerAndDistributor(savedProcess, transport.getCompanyName(), transport.getPhone(),
                "We got goods in Producer",
                "Nofication from " + transport.getCompanyName());
        return new ResponseModel("The Goods was taken by transport", HttpStatus.OK.value(), id);
    }

    @Transactional
    public ResponseModel confirmToReceiptGoods(Long id) {
        Process process = findProcessById(id);
        Distributor distributor = distributorService.getDistributorInPrincipal();
        if (!distributor.getId().equals(process.getDistributor().getId()) ||
                process.getStatusProcess() != StatusProcess.ON_BOARDING_REVEIVE) {
            return new ResponseModel("You don't have permission", HttpStatus.METHOD_NOT_ALLOWED.value(), id);
        }
        process.setReceipt_at(commonService.getDateTime());
        process.setStatusProcess(StatusProcess.REVEIVED);
        processRepository.save(process);
        List<QRCode> qrCodes = qrCodeRepository.findAllByProcess_Id(id);
        Map<Long, String> mapOtp = new HashMap<>();
        qrCodes.forEach(
                i -> {
                    i.setStatusQRCode(StatusQRCode.READY);
                    i.setOtp(generateOtp());
                    qrCodeRepository.save(i);
                    mapOtp.put(i.getId(), i.getOtp());
                }
        );
        blockchainService.updateProcess(distributor.getUser(), process.getId(), StatusProcess.REVEIVED,
                null, null, null, null, process.getReceipt_at(), distributor.getOrgMsp(), "kdtrace");
        blockchainService.saveQRCodes(distributor.getUser(), null, StatusQRCode.READY, mapOtp, "Org1","kdtrace");
        return new ResponseModel("The Goods was receipted by distributor", HttpStatus.OK.value(), id);
    }

    private String generateOtp() {
        SplittableRandom splittableRandom = new SplittableRandom();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 6; i++) {
            stringBuilder.append(splittableRandom.nextInt(0, 10));
        }
        return stringBuilder.toString();
    }

    private Process findProcessById(Long id) {
        return processRepository.findProcessById(id).orElseThrow(
                () -> new RecordNotFoundException("Not Found")
        );
    }

    public List<QRCode> getQRCodeByProcessId(Long id) {
        return qrCodeRepository.findAllByProcess_Id(id);
    }

    public ResponseModel getInfomation(String code) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElseThrow(
                () -> new RecordNotFoundException("Don't found QRCode with code")
        );

        if (qrCode.getStatusQRCode() != StatusQRCode.READY) {
            return new ResponseModel("Method is not allowed)", HttpStatus.METHOD_NOT_ALLOWED.value(), code);
        }

        Process process = qrCode.getProcess();
        DeliveryTruck deliveryTruck = transportService.findDeliveryTruckById(process.getDeliveryTruck().getId());
        Product product = productService.getProductEntityById(process.getProductID());
        product.setCodes(null);
        EndUserResponse response = new EndUserResponse(ProductMapper.INSTANCE.productToProductModel(product),
                ProducerMapper.INSTANCE.producerToProducerModel(product.getProducer()),
                process.getDelivery_at(),
                TransportMapper.INSTANCE.transportToTransportModel(deliveryTruck.getTransport()),
                DeliveryTruckMapper.INSTANCE.deliveryTruckToDeliveryTruckModel(deliveryTruck),
                process.getReceipt_at(),
                DistributorMapper.INSTANCE.distributorToDistributorModel(process.getDistributor()));
        return new ResponseModel("Successfully", HttpStatus.OK.value(), response);
    }
}
