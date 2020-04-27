package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.Producer;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.ProducerMapper;
import com.duytran.kdtrace.model.ProducerModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.ProducerRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
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
        if(!producerRepository.existsById(producerModel.getId())){
            return new ResponseModel("Not Exist Record to Update", 400, producerModel);
        }
        Producer producer = ProducerMapper.INSTANCE.producerToProducerModel(producerModel);
        producer.setUpdate_at(commonService.getDateTime());
        try{
            producerRepository.save(producer);
        }catch (Exception e){
            return new ResponseModel("Update not successfully", 400, e);
        }
        return new ResponseModel("Update sucessfully", 200, producer);
    }


    // Get Producer in Principal
    public Producer getProducerInPrincipal(){
        Producer producer = producerRepository.findProducerByUser_Username(userPrincipalService.getUserCurrentLogined())
                .orElseThrow(
                        () -> new RecordNotFoundException("Don't found producer")
                );
        return producer;
    }
}