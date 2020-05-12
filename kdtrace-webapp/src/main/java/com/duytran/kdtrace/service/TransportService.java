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
import org.springframework.stereotype.Service;

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


    public void createTransport(User user){
        Transport transport = new Transport();
        try {
            blockchainService.registerIdentity(user.getUsername(), transport.getOrgMsp());
        }catch (Exception e){
            throw new RuntimeException("Cannot register user identity");
        }
        transport.setCompanyName("Transport");
        transport.setUser(user);
        transportRepository.save(transport);
    }

    public ResponseModel getTransport(){
        Transport transport = getTransportInPrincipal();
        TransportModel transportModel = TransportMapper.INSTANCE.transportToTransportModel(transport);
        return new ResponseModel("Tranport Infomation", 200, transportModel);

    }


    public ResponseModel updateTransport(TransportModel transportModel){
        if(!transportRepository.existsById(transportModel.getId())){
            return new ResponseModel(" Not Exist Record To Update", 400, transportModel);
        }
        Transport transport = TransportMapper.INSTANCE.transportModelToTransport(transportModel);
        transport.setUpdate_at(commonService.getDateTime());
        try{
            blockchainService.updateTransport(userRepository.findByUsername(userPrincipalService.getUserCurrentLogined()).get(), transport, "kdtrace");
            transportRepository.save(transport);
        }catch (Exception e){
            return new ResponseModel("Update Not Successfully", 400, e);
        }
        return new ResponseModel("Update successfully", 200, transport);
    }


    public Transport getTransportInPrincipal(){
        Transport transport = transportRepository.findTransportByUser_Username(userPrincipalService.getUserCurrentLogined()).orElseThrow(
                () -> new RecordNotFoundException("Don't found transport")
        );
        return transport;
    }

    public ResponseModel createDeliveryTruck(DeliveryTruckModel deliveryTruckModel){
        if(deliveryTruckRepository.existsDeliveryTruckByNumberPlate(deliveryTruckModel.getNumberPlate())){
            return new ResponseModel("The number plate is exist", 400, deliveryTruckModel);
        }
        else{
            DeliveryTruck deliveryTruck = DeliveryTruckMapper.INSTANCE.deliveryTruckModelToDelivery(deliveryTruckModel);
            deliveryTruck.setTransport(getTransportInPrincipal());
            try{
                deliveryTruckRepository.save(deliveryTruck);
            }catch (Exception e){
                return new ResponseModel("Create not successfull", 400, e);
            }
            return new ResponseModel("Create successfully", 200, deliveryTruckModel);
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
        DeliveryTruck deliveryTruck = deliveryTruckRepository.findDeliveryTruckById(id).orElseThrow(
                () -> new RecordNotFoundException("Not Found")
        );
        return deliveryTruck;
    }
}
