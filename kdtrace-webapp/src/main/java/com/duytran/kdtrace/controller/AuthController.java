package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.LoginRequest;
import com.duytran.kdtrace.model.RegisterRequest;
import com.duytran.kdtrace.model.ResponseModel;
import com.duytran.kdtrace.security.jwt.JwtAuthenticationResponse;
import com.duytran.kdtrace.security.jwt.JwtTokenProvider;
import com.duytran.kdtrace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.validation.Valid;

@EnableSwagger2
@RestController
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("api/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest){
    return ResponseEntity.ok(userService.saveUser(registerRequest ));
}

    @PostMapping("api/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
        if(!userService.trackingActive(loginRequest.getUsername())){
            return ResponseEntity.ok( new ResponseModel("The account non-active", 400,
                                                        loginRequest.getUsername()));
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }
}