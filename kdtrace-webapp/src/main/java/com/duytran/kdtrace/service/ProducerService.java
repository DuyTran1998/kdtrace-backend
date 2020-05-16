package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.*;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.*;
import com.duytran.kdtrace.model.ProducerModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.ProducerRepository;
import com.duytran.kdtrace.security.principal.UserPrincipal;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProducerService {

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private CommonService commonService;


    // Function to create Producer Detail.

    public void createProducer(User user){
        Producer producer = new Producer();
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

    public ResponseModel updateProducer(ProducerModel producerModel){
        Producer producer =producerRepository.findProducerById(producerModel.getId()).orElseThrow(
                () -> new RecordNotFoundException("Producer isn't exist" + producerModel.getId())
        );

        producer.updateInformation( producer.getCompanyName(),
                                    producer.getEmail(),
                                    producer.getAddress(),
                                    producer.getPhone(),
                                    producer.getAvatar(),
                                    commonService.getDateTime());

        try{
            producerRepository.save(producer);
        }catch (Exception e){
            return new ResponseModel("Update not successfully", HttpStatus.BAD_REQUEST.value(), e);
        }
        return new ResponseModel("Update sucessfully", 200, producerModel);
    }


    // Get Producer in Principal

    public Producer getProducerInPrincipal(){
        String username = userPrincipalService.getUserCurrentLogined();
        Producer producer = producerRepository.findProducerByUser_Username(username)
                .orElseThrow(() -> new RecordNotFoundException("Producer isn't exist"));
        return producer;
    }

    public Producer findProducerById(Long id){
        Producer producer = producerRepository.findProducerById(id).orElseThrow(
                () -> new RecordNotFoundException("Producer isn't exist")
        );
        return producer;
    }

    public String getMail(Long id){
        String email = producerRepository.getProducerEmailByProductId(id);
        return email;
    }
}