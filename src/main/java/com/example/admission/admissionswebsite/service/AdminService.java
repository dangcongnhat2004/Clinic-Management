package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.OurUserRepo;
import com.example.admission.admissionswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;


    // Lấy danh sách userId của người dùng có role "university"
    public UserDto getUserIdsByUniversityRole() {
        UserDto resp = new UserDto();
        try {
            List<Users> users = userRepository.findByRoles("UNIVERSITY");
            if (users.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No users found with role 'university'");
                return resp;
            }
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(
                            Math.toIntExact(user.getId()),
                            user.getEmail(),
                            user.getFullName()
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
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
    public UserDto getUserIdsByUsersRole() {
        UserDto resp = new UserDto();
        try {
            List<Users> users = userRepository.findByRoles("STUDENT");
            if (users.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No users found with role 'STUDENT'");
                return resp;
            }
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(
                            Math.toIntExact(user.getId()),
                            user.getFullName(),
                            user.getEmail(),
                            user.getAddress(),
                            user.getBirthDate(),
                            user.getHighSchoolName(),
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
