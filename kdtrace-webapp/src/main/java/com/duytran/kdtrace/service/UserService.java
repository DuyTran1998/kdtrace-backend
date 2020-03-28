package com.duytran.kdtrace.service;

import com.duytran.kdtrace.entity.CompanyInfo;
import com.duytran.kdtrace.entity.Role;
import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordHasCreatedException;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.mapper.UserMapper;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.model.UserModel;
import com.duytran.kdtrace.repository.CompanyInfoRepository;
import com.duytran.kdtrace.repository.RoleRepository;
import com.duytran.kdtrace.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    /**
     * Save a user
     */
    @Transactional
    public ResponseModel saveUser(UserModel userModel){
        Role role = roleRepository.findByRoleName(userModel.getRole().getRoleName()).orElseThrow(
                () -> new RecordNotFoundException("Role don't exist in database")
        );
        User userCreated = userRepository.findByUsername(userModel.getUsername()).orElseThrow(
                () -> new RecordHasCreatedException("Username has been created")
        );
        User user = new User(userModel.getUsername(), passwordEncoder.encode(userModel.getPassword()));
        user.setRole(role);
        userRepository.save(user);
        saveCompanyInfo(user);
        return new ResponseModel("Successful", HttpStatus.OK.value(), user);
    }

    public void saveCompanyInfo(User user){
        CompanyInfo companyInfo = new CompanyInfo();
        companyInfo.setUser(user);
        companyInfo.setCompanyName("CP01");
        companyInfoRepository.save(companyInfo);
    }
}