package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.*;
import com.duytran.kdtrace.model.ProducerModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.ProducerRepository;
import com.duytran.kdtrace.repository.UserRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProducerService {

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private UserRepository userRepository;


    // Function to create Producer Detail.

    @Transactional
    public void createProducer(User user){
        Producer producer = new Producer();
        try {
            blockchainService.registerIdentity(user.getUsername(), producer.getOrgMsp());
        }catch (Exception e){
            throw new RuntimeException("Cannot register user identity");
        }
        producer.setCompanyName("Producer");
        producer.setCreate_at(commonService.getDateTime());
        producer.setUser(user);
        producerRepository.save(producer);
    }


    //  Get Information of Producer

    public ResponseModel getProducer(){
        Producer producer = getProducerInPrincipal();
        ProducerModel producerModel = ProducerMapper.INSTANCE.producerToProducerModel(producer);
        return new ResponseModel("Producer Information", 200, producerModel);
    }


    // Update Information of Producer
    @Transactional
    public ResponseModel updateProducer(ProducerModel producerModel){
        Producer producer =producerRepository.findProducerById(producerModel.getId()).orElseThrow(
                () -> new RecordNotFoundException("Producer isn't exist" + producerModel.getId())
        );

        producer.updateInformation( producerModel.getCompanyName(),
                                    producerModel.getEmail(),
                                    producerModel.getAddress(),
                                    producerModel.getPhone(),
                                    producerModel.getAvatar(),
                                    commonService.getDateTime());

        try{
            blockchainService.updateProducer(userRepository.findByUsername(userPrincipalService.getUserCurrentLogined()).get(), producer, "kdtrace");
            producerRepository.save(producer);
        }catch (Exception e){
            return new ResponseModel("Update not successfully", HttpStatus.BAD_REQUEST.value(), e);
        }
        return new ResponseModel("Update sucessfully", 200, producerModel);
    }


    // Get Producer in Principal

    public Producer getProducerInPrincipal(){
        String username = userPrincipalService.getUserCurrentLogined();
        return producerRepository.findProducerByUser_Username(username)
                .orElseThrow(() -> new RecordNotFoundException("Producer isn't exist"));
    }

//    public Producer findProducerById(Long id){
//        return producerRepository.findProducerById(id).orElseThrow(
//                () -> new RecordNotFoundException("Producer isn't exist")
//        );
//    }

    public String getMail(Long id){
        return producerRepository.getProducerEmailByProductId(id);
    }

    public String getAddress(Long id){
        return producerRepository.getProducerAddressByProductId(id);
    }
}
