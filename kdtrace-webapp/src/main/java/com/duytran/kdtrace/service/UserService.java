package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.Role;
import com.duytran.kdtrace.entity.RoleName;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.UserMapper;
import com.duytran.kdtrace.model.RegisterRequest;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.model.UserModel;
import com.duytran.kdtrace.repository.RoleRepository;
import com.duytran.kdtrace.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProducerService producerService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private DistributorService distributorService;

    @Autowired
    PasswordEncoder passwordEncoder;


    // Create new account for user.

    @Transactional
    public ResponseModel saveUser(RegisterRequest registerRequest){
        if(userRepository.existsUserByUsername(registerRequest.getUsername())){
            return new ResponseModel("Username have been created", HttpStatus.CREATED.value(), registerRequest.getUsername());
        }
        User user = new User(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()));
        Role role = roleRepository.findByRoleName(RoleName.ROLE_USER).orElseThrow(
                () -> new RecordNotFoundException("RoleName isn't exist")
        );
        user.setRole(role);
        userRepository.save(user);

        return new ResponseModel("Successful", HttpStatus.OK.value(), user);
    }


    //  Tracking to check account ? actived

    public boolean trackingActive(String username){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RecordNotFoundException("The account don't exist")
        );
        return user.isActive();
    }


    //  Get User By Id

    public UserModel getUserById(long id){
        User user = userRepository.findUserById(id).orElseThrow(
                () ->  new RecordNotFoundException("User Not Found")
        );
        UserModel userModel = UserMapper.INSTANCE.userToUserModel(user);
        return userModel;
    }

    //  Update User Following by UserModel.
    public ResponseModel updateUser(UserModel userModel){
        User user = userRepository.findUserById(userModel.getId()).orElseThrow(
                () -> new RecordNotFoundException("User Not Found")
        );

        //  Checking to make sure request have allow to change role of user.
        if(userModel.getRole() == null){
            if(user.getRole().getRoleName() == RoleName.ROLE_USER){
                user.setActive(false);
            }
            else{
                user.setActive(userModel.isActive());
            }
        }
        else{
            if(user.getRole().getRoleName() != RoleName.ROLE_USER){
                return new ResponseModel("The account don't allow to change role.", 403, userModel);
            }
            Role role = roleRepository.findByRoleName(userModel.getRole().getRoleName()).orElseThrow(
                    () -> new RecordNotFoundException("Role isn't exist")
            );
            user.setRole(role);
            user.setActive(userModel.isActive());
            switch (userModel.getRole().getRoleName()){
                    //    Create the description for user have ROLE_PRODUCER
                case ROLE_PRODUCER:
                    producerService.createProducer(user);
                    //    Create the description for user have ROLE_TRANSPORT
                case ROLE_TRANSPORT:
                    transportService.createTransport(user);
                    //    Create the description for user have ROLE_DISTRIBUTOR
                case ROLE_DISTRIBUTOR:
                    distributorService.createDistributor(user);
            }
        }
        try{
            userRepository.save(user);
        }catch (Exception e){
            return new ResponseModel("Update Fail", 400, userModel);
        }
        return new ResponseModel("Update Successfull", 200, user);
    }


    public ResponseModel getAllUser(){
        List<UserModel> userModels = UserMapper.INSTANCE.userListToUserModelList(userRepository.findAll());
        return new ResponseModel("Get All of Users", 200, userModels);
    }
}