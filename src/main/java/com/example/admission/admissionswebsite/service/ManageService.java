package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ManageService {
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDto signUp(UserDto registrationRequest){
        UserDto resp = new UserDto();
        if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
            resp.setStatusCode(400);
            resp.setMessage("Email already registered");
            return resp;
        }

        try {
            Users ourUsers = new Users();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRoles("DOCTOR");
            ourUsers.setFullName(registrationRequest.getFullName());
            ourUsers.setBirthDate(registrationRequest.getBirthDate());
            ourUsers.setGender(registrationRequest.getGender());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
            Users ourUserResult = ourUserRepo.save(ourUsers);

            if (ourUserResult != null && ourUserResult.getId()>0) {
                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Error during user registration: " + e.getMessage());
        }

        return resp;
    }
}
