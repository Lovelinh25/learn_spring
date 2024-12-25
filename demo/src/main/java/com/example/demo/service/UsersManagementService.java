package com.example.demo.service;

import com.example.demo.dto.ReqRes;
import com.example.demo.entity.Users;
import com.example.demo.repository.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class UsersManagementService {

    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ReqRes register(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();

        try {
            Users users1 = new Users();
            users1.setEmail(registrationRequest.getEmail());
            users1.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            users1.setCity(registrationRequest.getCity());
            users1.setName(registrationRequest.getName());
            users1.setRole(registrationRequest.getRole());
            users1.setOtp(registrationRequest.getOtp());
            Users usersResult = usersRepo.save(users1);

            if (usersResult.getId() > 0) {
                resp.setUsers(usersResult);
                resp.setMessage("User saved successfully");
                resp.setStatusCode(200);
            } else {
                resp.setMessage("User not saved");
                resp.setStatusCode(400);
            }

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("An error occurred while saving the user: " + e.getMessage());
        }
        return resp;
    }

    public ReqRes login(ReqRes loginRequest) {
        ReqRes response = new ReqRes();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            Users user = usersRepo.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String jwt = jwtUtils.generateToken(user);
            String refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hrs");
            response.setMessage("Successfully logged in");
            response.setStatusCode(200);
        } catch (BadCredentialsException e) {
            response.setStatusCode(401);
            response.setError("Login failed: Bad credentials");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Login failed: " + e.getMessage());
        }
        return response;
    }


    public ReqRes refreshToken(ReqRes refreshTokenRequest) {
        ReqRes response = new ReqRes();
        try {
            String userEmail = jwtUtils.extractUsername(refreshTokenRequest.getToken());
            Users user = usersRepo.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
            if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
                String jwt = jwtUtils.generateToken(user);
                response.setToken(jwt);
                response.setRefreshToken(refreshTokenRequest.getToken());
                response.setExpirationTime("24Hrs");
                response.setMessage("Successfully refreshed token");
                response.setStatusCode(200);
            } else {
                response.setMessage("Invalid token");
                response.setStatusCode(401);
            }
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Token refresh failed: " + e.getMessage());
        }
        return response;
    }

    public ReqRes getAllUsers() {
        ReqRes reqRes = new ReqRes();
        try {
            List<Users> result = usersRepo.findAll();
            if (!result.isEmpty()) {
                reqRes.setUsersList(result);
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successful");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("No users found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setMessage("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getUserById(Integer id) {
        ReqRes reqRes = new ReqRes();
        try {
            Users usersById = usersRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            reqRes.setUsers(usersById);
            reqRes.setStatusCode(200);
            reqRes.setMessage("User with ID '" + id + "' found successfully");
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setError("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes deleteUser(Integer userId) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Users> usersOptional = usersRepo.findById(userId);
            if (usersOptional.isPresent()) {
                usersRepo.deleteById(userId);
                reqRes.setStatusCode(200);
                reqRes.setMessage("User deleted successfully");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found for deletion");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setError("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes updateUser(Integer userId, Users updateUser) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Users> usersOptional = usersRepo.findById(userId);
            if (usersOptional.isPresent()) {
                Users existingUser = usersOptional.get();
                existingUser.setEmail(updateUser.getEmail());
                existingUser.setName(updateUser.getName());
                existingUser.setCity(updateUser.getCity());
                existingUser.setRole(updateUser.getRole());
                existingUser.setOtp(updateUser.getOtp());

                if (updateUser.getPassword() != null && !updateUser.getPassword().isEmpty()) {
                    existingUser.setPassword(passwordEncoder.encode(updateUser.getPassword()));
                }

                usersRepo.save(existingUser);
                reqRes.setMessage("User updated successfully");
                reqRes.setStatusCode(200);
            } else {
                reqRes.setMessage("User not found for update");
                reqRes.setStatusCode(404);
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setError("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes getMyInfo(String email) {
        ReqRes reqRes = new ReqRes();
        try {
            Optional<Users> usersOptional = usersRepo.findByEmail(email);
            if (usersOptional.isPresent()) {
                reqRes.setUsers(usersOptional.get());
                reqRes.setStatusCode(200);
                reqRes.setMessage("Successfully retrieved user information");
            } else {
                reqRes.setStatusCode(404);
                reqRes.setMessage("User not found");
            }
        } catch (Exception e) {
            reqRes.setStatusCode(500);
            reqRes.setError("Error occurred: " + e.getMessage());
        }
        return reqRes;
    }

    public ReqRes sendOtp(ReqRes otpRequest) {
        ReqRes response = new ReqRes();
        try {
            String otp = otpRequest.getOtp(); // Modify as per your OTP generation logic
            response.setOtp(otp);
            response.setMessage("OTP sent successfully");
            response.setStatusCode(200);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError("Error occurred while sending OTP: " + e.getMessage());
        }
        return response;
    }
}
