package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.Distributor;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.DistributorMapper;
import com.duytran.kdtrace.model.DistributorModel;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.repository.DistributorRepository;
import com.duytran.kdtrace.security.principal.UserPrincipalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistributorService {

    @Autowired
    private DistributorRepository distributorRepository;

    @Autowired
    private UserPrincipalService userPrincipalService;

    @Autowired
    private BlockchainService blockchainService;

    @Autowired
    private CommonService commonService;

    public void createDistributor(User user){
        Distributor distributor = new Distributor();
        try {
            blockchainService.registerIdentity(user.getUsername(), distributor.getOrgMsp());
        }catch (Exception e){
            throw new RuntimeException("Cannot register user identity");
        }
        distributor.setCompanyName("Distributor");
        distributor.setUser(user);
        distributorRepository.save(distributor);
    }


    public ResponseModel getDistributor(){
       Distributor distributor = getDistributorInPrincipal();
       DistributorModel distributorModel = DistributorMapper.INSTANCE.distributorToDistributorModel(distributor);
       return new ResponseModel("Distributor Information", 200, distributorModel);
    }


    public ResponseModel updateDistriButor(DistributorModel distributorModel){
        if(! distributorRepository.existsById(distributorModel.getId())){
            return new ResponseModel(" Not Exist Record to Update", 400, distributorModel);
        }
        Distributor distributor = DistributorMapper.INSTANCE.distributorModelToDistributor(distributorModel);
        distributor.setUpdate_at(commonService.getDateTime());
        try{
            distributorRepository.save(distributor);
        }catch (Exception e){
            return new ResponseModel("Update Not Successfully", 400, e);
        }
        return new ResponseModel("Update Successfully ", 200, distributor);
    }


    public Distributor getDistributorInPrincipal(){
        Distributor distributor =  distributorRepository.findDistributorByUser_Username(userPrincipalService.getUserCurrentLogined()).orElseThrow(
                () -> new RecordNotFoundException("Don't found distributor")
        );
        return distributor;
    }
}