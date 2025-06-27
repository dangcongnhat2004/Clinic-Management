package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.NurseRepository;
import com.example.admission.admissionswebsite.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class NurseManageService {
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private NurseRepository nurseRepository;

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
            ourUsers.setRoles("NURSE");
            ourUsers.setFullName(registrationRequest.getFullName());
            ourUsers.setBirthDate(registrationRequest.getBirthDate());
            ourUsers.setGender(registrationRequest.getGender());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
            ourUsers.setOccupation(registrationRequest.getOccupation());
            ourUsers.setAddress(registrationRequest.getAddress());
            ourUsers.setStatus("ACTIVE");
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

    public UserDto getUserIdsByUsersRole() {
        UserDto resp = new UserDto();
        try {
            List<Users> users = nurseRepository.findByRoles("NURSE");
            if (users.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No users found with role 'NURSE'");
                return resp;
            }
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(
                            Math.toIntExact(user.getId()),
                            user.getFullName(),
                            user.getEmail(),
                            user.getAddress(),
                            user.getBirthDate(),
                            user.getOccupation(),
                            user.getPhoneNumber()

                    ))
                    .collect(Collectors.toList());
            resp.setStatusCode(200);
            resp.setMessage("User IDs retrieved successfully");
            resp.setOurUser(userDtos); // Đảm bảo trả về danh sách UserDto
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Error while retrieving user IDs: " + e.getMessage());
        }
        return resp;
    }

}
