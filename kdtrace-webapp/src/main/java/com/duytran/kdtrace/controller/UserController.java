package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.model.UserModel;
import com.duytran.kdtrace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/get")
    public ResponseEntity<?> getUserById(@RequestParam long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("getAllUsers")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateRoleForUser(@RequestBody UserModel userModel){
        return ResponseEntity.ok(userService.updateUser(userModel));
    }
}