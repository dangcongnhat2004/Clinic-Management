package com.clinicmanagement.service;

import com.clinicmanagement.Dto.ReqRes;
import com.clinicmanagement.Model.OurUsers;
import com.clinicmanagement.Model.Role;
import com.clinicmanagement.Model.User;
import com.clinicmanagement.Model.UserRole;
import com.clinicmanagement.repository.OurUserRepo;
import com.clinicmanagement.repository.RoleRepository;
import com.clinicmanagement.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepo ourUser;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Transactional
    public ReqRes signUp(ReqRes registrationRequest){
        ReqRes resp = new ReqRes();
        try {
            // Validate role
            String requestedRole = registrationRequest.getName();
            boolean isValidRole = false;
            for (UserRole role : UserRole.values()) {
                if (role.getRoleName().equals(requestedRole)) {
                    isValidRole = true;
                    break;
                }
            }
            
            if (!isValidRole) {
                resp.setStatusCode(400);
                resp.setError("Invalid role. Allowed roles are: ROLE_ADMIN, ROLE_USER, ROLE_DOCTOR, ROLE_NURSE, ROLE_STAFF");
                return resp;
            }

            // Get or create role
            Optional<Role> existingRole = roleRepository.findByName(requestedRole);
            Role role;
            if (existingRole.isPresent()) {
                role = existingRole.get();
            } else {
                role = new Role();
                role.setName(requestedRole);
                role = roleRepository.save(role);
            }

            // Create and save user
            User ourUsers = new User();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setUsername(registrationRequest.getEmail());
            
            // Add role to user's roles set
            ourUsers.getRoles().add(role);
            
            User ourUserResult = ourUser.save(ourUsers);
            if (ourUserResult != null && ourUserResult.getId()>0) {
                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e){
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signinRequest){
        ReqRes response = new ReqRes();

        try {
            // First check if user exists
            var userOptional = ourUser.findByEmail(signinRequest.getEmail());
            if (userOptional.isEmpty()) {
                response.setStatusCode(401);
                response.setError("User not found with email: " + signinRequest.getEmail());
                return response;
            }

            var user = userOptional.get();
            System.out.println("Found user: " + user.getEmail());
            System.out.println("Stored password: " + user.getPassword());
            System.out.println("Input password: " + signinRequest.getPassword());

            // Try to authenticate
            try {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));
            } catch (Exception e) {
                System.out.println("Authentication failed: " + e.getMessage());
                response.setStatusCode(401);
                response.setError("Invalid email or password");
                return response;
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
        } catch (Exception e) {
            System.out.println("Unexpected error during sign in: " + e.getMessage());
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("An error occurred during sign in: " + e.getMessage());
        }
        return response;
    }

    public ReqRes refreshToken(ReqRes refreshTokenReqiest){
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        User users = ourUser.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), users)) {
            var jwt = jwtUtils.generateToken(users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }
}
