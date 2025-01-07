package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UniversityService {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private UserRepository userRepository;
    @Value("${upload.path}")
    private String uploadPath;
    public UniversityDto addUniversity(UniversityDto universityDto, MultipartFile file) {
        UniversityDto response = new UniversityDto();
        try {
            System.out.println("Start: addUniversity method");

            // Retrieve the user linked to the university
            Users user = userRepository.findById(universityDto.getUserId()).orElse(null);
            if (user == null) {
                System.out.println("User not found");
                response.setStatusCode(404);
                response.setMessage("User not found");
                return response;
            }

            System.out.println("User found: " + user.getUsername());

            // Create University object from UniversityDto
            University university = new University();
            university.setNameSchool(universityDto.getNameSchool());
            university.setAddress(universityDto.getAddress());
            university.setDescription(universityDto.getDescription());

            System.out.println("University details set");

            // Save the logo file to the project folder and set it in the university
            if (file != null && !file.isEmpty()) {
                System.out.println("File uploaded: " + file.getOriginalFilename());
                String fileName = file.getOriginalFilename();
                Path uploadDirPath = Paths.get(uploadPath);

                // Create directory if it doesn't exist
                if (!Files.exists(uploadDirPath)) {
                    Files.createDirectories(uploadDirPath);
                    System.out.println("Directory created: " + uploadDirPath.toAbsolutePath());
                }

                // Save file to server
                Path filePath = uploadDirPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Log for checking
                System.out.println("File saved at: " + filePath.toAbsolutePath());

                // Set file path in database
                university.setUniversityLogo(fileName);
            } else {
                System.out.println("No file uploaded");
            }

            university.setUsers(user);

            // Save to database
            University savedUniversity = universityRepository.save(university);

            System.out.println("University saved with ID: " + savedUniversity.getId());

            // Set response details
            response.setStatusCode(200);
            response.setMessage("University added successfully");
            response.setId(savedUniversity.getId());
            response.setNameSchool(savedUniversity.getNameSchool());
            response.setAddress(savedUniversity.getAddress());
            response.setDescription(savedUniversity.getDescription());

        } catch (Exception e) {
            System.err.println("Error during university registration: " + e.getMessage());
            e.printStackTrace();
            response.setStatusCode(500);
            response.setError("Error during university registration: " + e.getMessage());
        }
        return response;
    }



//    public List<UniversityDto> getAllUniversities() {
//        return universityRepository.findAll().stream()
//                .map(UniversityDto::new)
//                .collect(Collectors.toList());
//    }
}
