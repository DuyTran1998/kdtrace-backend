package com.duytran.kdtrace.controller;

import com.duytran.kdtrace.service.ProductService;
import com.duytran.kdtrace.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;

    @GetMapping("/get")
    public ResponseEntity<?> getUserById(@RequestParam Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("getAllUsers")
    public ResponseEntity<?> getAllUser(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @PostMapping("/active")
    public ResponseEntity<?> updateRoleForUser(@Valid @RequestParam Long user_id){
        return ResponseEntity.ok(userService.activeUser(user_id));
    }

    @GetMapping("getProfile")
    public ResponseEntity<?> getProfile(@RequestParam String username, String role){
        return ResponseEntity.ok(userService.getProfile(username, role));
    }

    @GetMapping("getAllReports")
    public ResponseEntity<?> getAllReports(){
        return ResponseEntity.ok(productService.getAllReports());
    }
}
