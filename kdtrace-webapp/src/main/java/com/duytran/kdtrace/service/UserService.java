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
import com.duytran.kdtrace.security.principal.UserPrincipalService;
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
    private UserPrincipalService userPrincipalService;

    @Autowired
    PasswordEncoder passwordEncoder;


    // Create new account for user.

    @Transactional
    public ResponseModel saveUser(RegisterRequest registerRequest){
        if(userRepository.existsUserByUsername(registerRequest.getUsername())){
            return new ResponseModel("Username have been created", HttpStatus.BAD_REQUEST.value(), registerRequest.getUsername());
        }
        User user = new User(registerRequest.getUsername(), passwordEncoder.encode(registerRequest.getPassword()),
                                                                                            registerRequest.getEmail());
        Role role = roleRepository.findByRoleName(registerRequest.getRoleName()).orElseThrow(
                () -> new RecordNotFoundException("RoleName isn't exist")
        );
        user.setRole(role);
        User savedUser = userRepository.save(user);
        switch (savedUser.getRole().getRoleName()){
            //    Create the description for user have ROLE_PRODUCER
            case ROLE_PRODUCER:
                producerService.createProducer(user);
                break;
            //    Create the description for user have ROLE_TRANSPORT
            case ROLE_TRANSPORT:
                transportService.createTransport(user);
                break;
            //    Create the description for user have ROLE_DISTRIBUTOR
            case ROLE_DISTRIBUTOR:
                distributorService.createDistributor(user);
                break;
        }

        return new ResponseModel("Successful", HttpStatus.OK.value(),  UserMapper.INSTANCE.userToUserDto(user));
    }


    //  Tracking to check account ? actived

    public boolean trackingActive(String username){
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new RecordNotFoundException("The account don't exist")
        );
        return user.isActive();
    }


    //  Get User By Id

    public UserModel getUserById(Long id){
        User user = userRepository.findUserById(id).orElseThrow(
                () ->  new RecordNotFoundException("User Not Found")
        );
        return UserMapper.INSTANCE.userToUserModel(user);
    }

    public UserModel getUserLogged(){
        Long id = userPrincipalService.getUserIdCurrentLogined();
        return getUserById(id);
    }

    //  Active User by role_admin
    public ResponseModel activeUser(Long user_id){
        User user = userRepository.findUserById(user_id).orElseThrow(
                () -> new RecordNotFoundException("User Not Found")
        );
        user.setActive(!user.isActive());

        try{
            userRepository.save(user);
        }catch (Exception e){
            return new ResponseModel("Update Fail", 400, user_id);
        }
        return new ResponseModel("Update Successfull", 200, UserMapper.INSTANCE.userToUserDto(user));
    }


    public ResponseModel getAllUser(){
        List<UserModel> userModels = UserMapper.INSTANCE.userListToUserModelList(userRepository.findAllByOrderByIdDesc());
        return new ResponseModel("Get All of Users", 200, userModels);
    }
}
