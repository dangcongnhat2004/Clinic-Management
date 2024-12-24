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

            // Lấy thông tin người dùng liên kết
            Users user = userRepository.findById(universityDto.getUserId()).orElse(null);
            if (user == null) {
                System.out.println("User not found");
                response.setStatusCode(404);
                response.setMessage("User not found");
                return response;
            }

            System.out.println("User found: " + user.getUsername());

            // Tạo đối tượng University từ UniversityDto
            University university = new University();
            university.setNameSchool(universityDto.getNameSchool());
            university.setAddress(universityDto.getAddress());
            university.setDescription(universityDto.getDescription());

            System.out.println("University details set");

            // Lưu file logo vào folder trong project và set vào university
            if (file != null && !file.isEmpty()) {
                System.out.println("File uploaded: " + file.getOriginalFilename());
                String fileName = file.getOriginalFilename();
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);

                // Tạo thư mục nếu chưa tồn tại
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                    System.out.println("Directory created: " + uploadPath.toAbsolutePath());
                }

                // Lưu file lên server
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // In log kiểm tra
                System.out.println("File saved at: " + filePath.toAbsolutePath());

                // Gán đường dẫn file vào database
                university.setUniversityLogo(fileName);
            } else {
                System.out.println("No file uploaded");
            }

            university.setUsers(user);

            // Lưu vào cơ sở dữ liệu
            University savedUniversity = universityRepository.save(university);

            System.out.println("University saved with ID: " + savedUniversity.getId());

            // Thiết lập thông tin phản hồi
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



    public List<UniversityDto> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityDto::new)
                .collect(Collectors.toList());
    }
}
