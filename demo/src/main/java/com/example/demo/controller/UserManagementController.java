package com.example.demo.controller;

import com.example.demo.dto.ReqRes;
import com.example.demo.entity.Users;
import com.example.demo.service.UsersManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserManagementController {

    @Autowired
    private UsersManagementService usersManagementService;

    @PostMapping("/auth/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.register(reg));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.login(reg));
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes reg) {
        return ResponseEntity.ok(usersManagementService.refreshToken(reg));
    }

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }

    @GetMapping("/admin/get-user/{userId}")
    public ResponseEntity<ReqRes> getUserById(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(usersManagementService.getUserById(userId));
    }

    @PutMapping("/admin/update/{userId}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable("userId") Integer userId, @RequestBody Users updateUser) {
        return ResponseEntity.ok(usersManagementService.updateUser(userId, updateUser));
    }

    @DeleteMapping("/admin/delete/{userId}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(usersManagementService.deleteUser(userId));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        ReqRes response = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @PostMapping("/auth/send-otp")
    public ResponseEntity<ReqRes> sendOtp(@RequestBody ReqRes otpRequest) {
        return ResponseEntity.ok(usersManagementService.sendOtp(otpRequest));
    }
}
