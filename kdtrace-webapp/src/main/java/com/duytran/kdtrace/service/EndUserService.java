package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.LedgerMapper;
import com.duytran.kdtrace.mapper.ProductMapper;
import com.duytran.kdtrace.model.ProcessModel;
import com.duytran.kdtrace.model.RequestReport;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.*;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
public class EndUserService {

    @Autowired
    QRCodeRepository qrCodeRepository;

    @Autowired
    CommonService commonService;

    @Autowired
    BlockchainService blockchainService;

    @Autowired
    ProducerRepository producerRepository;

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    TransportRepository transportRepository;

    @Autowired
    DistributorRepository distributorRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseModel getRootProduct(String code) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElseThrow(
                () -> new RecordNotFoundException("Don't found QRCode with code")
        );
        if (qrCode.getStatusQRCode() != StatusQRCode.READY) {
            return new ResponseModel("Method is not allowed)", HttpStatus.METHOD_NOT_ALLOWED.value(), code);
        }
        return new ResponseModel(
                "Root Product",
                HttpStatus.OK.value(),
                ProductMapper.INSTANCE.productToProductModel(qrCode.getProduct()));
    }

    public ResponseModel getInfomation(String code) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElseThrow(
                () -> new RecordNotFoundException("Don't found QRCode with code")
        );
        if (qrCode.getStatusQRCode() != StatusQRCode.READY) {
            return new ResponseModel("Method is not allowed)", HttpStatus.METHOD_NOT_ALLOWED.value(), code);
        }
        User user1 = userRepository.findByUsername("enduser-kdtrace1").orElse(new User());
        User user2 = userRepository.findByUsername("enduser-kdtrace2").orElse(new User());
        User user3 = userRepository.findByUsername("enduser-kdtrace3").orElse(new User());
        LedgerQRCode ledgerQRCode = blockchainService.getQRCode(
                user1, qrCode.getId(), "Org1");
        LedgerProcess ledgerProcess = blockchainService.getProcess(
                user3, ledgerQRCode.getProcessId(), "Org3");
        LedgerProduct ledgerProduct = blockchainService.getProduct(
                user1, ledgerQRCode.getProductId(), "Org1");
        LedgerProducer ledgerProducer = blockchainService.getProducer(
                user1, producerRepository.findProducerById(ledgerProduct.getProducerId()).get().getUser().getId(), "Org1");
        LedgerTransport ledgerTransport = blockchainService.getTransport(
                user2, transportRepository.findTransportById(ledgerProcess.getTransportId()).get().getUser().getId(), "Org2");
        LedgerDeliveryTruck ledgerDeliveryTruck = blockchainService.getDeliveryTruck(
                user2, ledgerProcess.getDeliveryTruckId(), "Org2");
        LedgerDistributor ledgerDistributor = blockchainService.getDistributor(
                user3, qrCode.getProcess().getDistributor().getUser().getId(), "Org3");

        ProcessModel processModel = new ProcessModel(
                ledgerProcess.getId(),
                ledgerProduct.getId(),
                ledgerTransport.getTransportId(),
                StatusProcess.valueOf(ledgerProcess.getStatusProcess()),
                ledgerProcess.getDelivery_at(),
                ledgerProcess.getReceipt_at(),
                qrCode.getProcess().getQuanlity(),
                ledgerProcess.getCreate_at(),
                new ArrayList<>(),
                LedgerMapper.INSTANCE.toProducerModel(ledgerProducer),
                LedgerMapper.INSTANCE.toTransportModel(ledgerTransport),
                LedgerMapper.INSTANCE.toDeliveryTruckModel(ledgerDeliveryTruck),
                LedgerMapper.INSTANCE.toDistributorModel(ledgerDistributor),
                LedgerMapper.INSTANCE.toProductModel(ledgerProduct),
                qrCode.getProcess().getUpdateAt()
        );
        processModel.getProductModel().setImage(qrCode.getProduct().getImage());
        return new ResponseModel("Process", HttpStatus.OK.value(), processModel);
    }

    public Long trackingCode(String code, String otp) {
        QRCode qrCode = qrCodeRepository.findByCode(code).orElse(new QRCode());
        if (otp.equals(qrCode.getOtp())) {
            if (qrCode.getTracking() == null)
                qrCode.setTracking(1L);
            else
                qrCode.setTracking(qrCode.getTracking() + 1);
            return qrCodeRepository.save(qrCode).getTracking();
        }
        return null;
    }

    public String updateReport(RequestReport requestReport) {
        QRCode qrCode = qrCodeRepository.findByCode(requestReport.getCode()).orElse(null);
        if (qrCode == null || qrCode.getStatusQRCode()!= StatusQRCode.READY) {
            return "QRCode not found!";
        }
        Report report = ProductMapper.INSTANCE.requestReportToReport(requestReport);
        report.setProductLink(qrCode.getLink() );
        report.setTime(commonService.getDateTime());
        report.setCode(qrCode);
        reportRepository.save(report);

        Producer producer = qrCode.getProduct().getProducer();
        producer.updateRate(requestReport.getRate());
        producerRepository.save(producer);

        Transport transport = qrCode.getProcess().getDeliveryTruck().getTransport();
        transport.updateRate(requestReport.getRate());
        transportRepository.save(transport);

        Distributor distributor = qrCode.getProcess().getDistributor();
        distributor.updateRate(requestReport.getRate());
        distributorRepository.save(distributor);

        return "Submit report for " + qrCode.getProduct().getName() + " successfully. Management department will check and overcome that is concerned. Thank you!";
    }

    public Float getRate(Long id, RoleName roleName){
        switch (roleName){
            case ROLE_PRODUCER:
                return producerRepository.findProducerById(id).get().getRate();
            case ROLE_TRANSPORT:
                return transportRepository.findTransportById(id).get().getRate();
            case ROLE_DISTRIBUTOR:
                return distributorRepository.findDistributorById(id).get().getRate();
        }
        return 5F;
    }
}
