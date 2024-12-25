package com.example.demo.service;

import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepo;
import com.example.demo.sendOtp.OtpGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsersDetailsService implements UserDetailsService {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Tìm kiếm người dùng theo email
        Users user = usersRepo.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        // Tạo và gửi OTP
        String otp = OtpGenerator.generateOtp();
        emailService.sendOtpEmail(user.getEmail(), otp);

        // Trả về thông tin người dùng
        return user;
    }
}
