package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.entity.Process;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.DeliveryTruckMapper;
import com.duytran.kdtrace.mapper.DistributorMapper;
import com.duytran.kdtrace.mapper.ProcessMapper;
import com.duytran.kdtrace.mapper.ProductMapper;
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
    private BlockchainService blockchainService;

    @Transactional
    public ResponseModel createProcess(RequestProcessModel processModel){
        if(productService.checkQuanlityProducts(processModel.getId_product(), processModel.getQuantity())){
            Distributor distributor =  distributorService.getDistributorInPrincipal();
            Process process = new Process();
            process.setQuanlity(processModel.getQuantity());
            process.setDistributor(distributor);
            process.setProduct(productService.getProductById(processModel.getId_product()));
            process.setStatusProcess(StatusProcess.WAITING_RESPONSE_PRODUCER);
            processRepository.save(process);
            blockchainService.updateProcess(distributor.getUser(), process, "kdtrace");
            return new ResponseModel("Sucessfull", HttpStatus.OK.value(), processModel);
        }
        return new ResponseModel("Don't enough quanlity product to buy", HttpStatus.BAD_REQUEST.value(), processModel);
    }

    public ResponseModel getProcess(Long id){
        Process process = findProcessById(id);
        ProcessModel processModel = ProcessMapper.INSTANCE.processToProcessModel(process);
        processModel.setDistributorModel(DistributorMapper.INSTANCE.distributorToDistributorModel(process.getDistributor()));
        processModel.setProductModel(ProductMapper.INSTANCE.productToProductModel(process.getProduct()));
        processModel.setDeliveryTruckModel(DeliveryTruckMapper.INSTANCE.deliveryTruckToDeliveryTruckModel(process.getDeliveryTruck()));
        processModel.setQrCodeModels(ProductMapper.INSTANCE.qrCodeListToQRCodeModel(process.getQrCodes()));
        return new ResponseModel("Process", HttpStatus.OK.value(), processModel);
    }

    public ResponseModel acceptToSell(Long id){
        Process process = findProcessById(id);
        process.setStatusProcess(StatusProcess.CHOOSE_DELIVERYTRUCK_TRANSPORT);
        List<QRCode> qrCodes = qrCodeRepository.getListQRCode(id);
        qrCodes.forEach(e -> {
            e.setProcess(process);
            qrCodeRepository.save(e);
        });
        processRepository.save(process);
        return new ResponseModel("Accepted", HttpStatus.OK.value(), id);
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
        return new ResponseModel("The Goods was receipted by distributor", HttpStatus.OK.value(), id);
    }

    public Process findProcessById(Long id){
        Process process = processRepository.findProcessById(id).orElseThrow(
                () -> new RecordNotFoundException("Process isn't found")
        );
        return process;
    }
}
