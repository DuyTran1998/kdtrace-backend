package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.DeliveryTruck;
import com.duytran.kdtrace.entity.Transport;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.DeliveryTruckMapper;
import com.duytran.kdtrace.mapper.TransportMapper;
import com.duytran.kdtrace.model.DeliveryTruckModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.model.TransportModel;
import com.duytran.kdtrace.repository.DeliveryTruckRepository;
import com.duytran.kdtrace.repository.TransportRepository;
import com.duytran.kdtrace.repository.UserRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private DeliveryTruckRepository deliveryTruckRepository;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void createTransport(User user){
        Transport transport = new Transport();
        try {
            blockchainService.registerIdentity(user.getUsername(), transport.getOrgMsp());
        }catch (Exception e){
            throw new RuntimeException("Cannot register user identity");
        }
        transport.setCompanyName("Transport");
        transport.setCreate_at(commonService.getDateTime());
        transport.setUser(user);
        transportRepository.save(transport);
    }

    public ResponseModel getTransport(){
        Transport transport = getTransportInPrincipal();
        TransportModel transportModel = TransportMapper.INSTANCE.transportToTransportModel(transport);
        return new ResponseModel("Tranport Infomation", 200, transportModel);

    }

    @Transactional
    public ResponseModel updateTransport(TransportModel transportModel){
        Transport transport = transportRepository.findTransportById(transportModel.getId()).orElseThrow(
                () -> new RecordNotFoundException("Transport isn't exist")
        );

        transport.updateInformation(transportModel.getCompanyName(),
                                    transportModel.getEmail(),
                                    transportModel.getAddress(),
                                    transportModel.getPhone(),
                                    transportModel.getAvatar(),
                                    commonService.getDateTime());

        try{
            blockchainService.updateTransport(userRepository.findByUsername(userPrincipalService.getUserCurrentLogined()).get(), transport, "kdtrace");
            transportRepository.save(transport);
        }catch (Exception e){
            return new ResponseModel("Update Not Successfully", 400, e);
        }
        return new ResponseModel("Update successfully", 200, transportModel);
    }


    public ResponseModel getAllTransports(){
        List<Transport> transports = transportRepository.findAll();
        List<TransportModel> transportModels = TransportMapper.INSTANCE.listTransportToListTransportModel(transports);
        return new ResponseModel( "List Transport Company", 200, transportModels);
    }


    public Transport getTransportInPrincipal(){
        return transportRepository.findTransportByUser_Username(userPrincipalService.getUserCurrentLogined()).orElseThrow(
                () -> new RecordNotFoundException("Don't found transport")
        );
    }

    @Transactional
    public ResponseModel createDeliveryTruck(DeliveryTruckModel deliveryTruckModel){
        if(deliveryTruckRepository.existsDeliveryTruckByNumberPlate(deliveryTruckModel.getNumberPlate())){
            return new ResponseModel("The number plate is exist", HttpStatus.BAD_REQUEST.value(),
                                                                                                    deliveryTruckModel);
        }
        else{
            Transport transport = getTransportInPrincipal();
            DeliveryTruck deliveryTruck = DeliveryTruckMapper.INSTANCE.deliveryTruckModelToDelivery(deliveryTruckModel);
            deliveryTruck.setTransport(transport);
            try{
                deliveryTruck.setCreate_at(commonService.getDateTime());
                deliveryTruckRepository.save(deliveryTruck);
                blockchainService.updateDeliveryTruck(transport.getUser(), deliveryTruck, transport.getOrgMsp(), "kdtrace");
            }catch (Exception e){
                return new ResponseModel("Don't create successfully", HttpStatus.BAD_REQUEST.value(), e);
            }
            return new ResponseModel("Create successfully", HttpStatus.CREATED.value(), deliveryTruckModel);
        }
    }

    public ResponseModel getDeliveryTruckList(){
        Long id = getTransportInPrincipal().getId();
        List<DeliveryTruck> deliveryTruckList = deliveryTruckRepository.findAllByTransport_Id(id);
        List<DeliveryTruckModel> deliveryTruckModelList = DeliveryTruckMapper.INSTANCE
                                                        .deliveryTruckListToDeliveryTruckModelList(deliveryTruckList);
        return new ResponseModel("Delivery Truck List", 200, deliveryTruckModelList);
    }

    public DeliveryTruck findDeliveryTruckById(Long id){
        return deliveryTruckRepository.findDeliveryTruckById(id).orElseThrow(
                () -> new RecordNotFoundException("Not Found")
        );
    }

    public String getEmailByTransportId(Long id){
        return transportRepository.getEmailByTransport_Id(id);
    }
}
