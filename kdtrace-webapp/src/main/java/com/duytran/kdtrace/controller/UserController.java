package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.entity.User;
import com.duytran.kdtrace.exeption.RecordNotFoundException;
import com.duytran.kdtrace.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/get")
    public ResponseEntity<?> getUserById(@RequestParam long id){
        User user = userRepository.findUserById(id).orElseThrow(
                () -> new RecordNotFoundException("NOT FOUND")
        );
        return ResponseEntity.ok(user);
    }
}