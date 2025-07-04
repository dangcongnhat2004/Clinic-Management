package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.DoctorRepository;
import com.example.admission.admissionswebsite.repository.DoctorsRepository;
import com.example.admission.admissionswebsite.repository.OurUserRepo;
import com.example.admission.admissionswebsite.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoctorManageService {
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorsRepository doctorsRepository;
    @Autowired
    private UserRepository userRepository;

    @Value("${upload.event}")
    private String uploadPath;
    private static final String UPLOAD_DIR = "src/main/resources/static/avatars/";

//    public UserDto signUp(UserDto registrationRequest){
//        UserDto resp = new UserDto();
//        if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
//            resp.setStatusCode(400);
//            resp.setMessage("Email already registered");
//            return resp;
//        }
//
//        try {
//            Users ourUsers = new Users();
//            ourUsers.setEmail(registrationRequest.getEmail());
//            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
//            ourUsers.setRoles("DOCTOR");
//            ourUsers.setFullName(registrationRequest.getFullName());
//            ourUsers.setBirthDate(registrationRequest.getBirthDate());
//            ourUsers.setGender(registrationRequest.getGender());
//            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
//            ourUsers.setOccupation(registrationRequest.getOccupation());
//            ourUsers.setAddress(registrationRequest.getAddress());
//            ourUsers.setStatus("ACTIVE");
//            Users ourUserResult = ourUserRepo.save(ourUsers);
//
//            if (ourUserResult != null && ourUserResult.getId()>0) {
//                resp.setOurUsers(ourUserResult);
//                resp.setMessage("User Saved Successfully");
//                resp.setStatusCode(200);
//            }
//        }catch (Exception e) {
//            resp.setStatusCode(500);
//            resp.setError("Error during user registration: " + e.getMessage());
//        }
//
//        return resp;
//    }
@Transactional // Thêm annotation này để đảm bảo cả 2 thao tác lưu là một giao dịch duy nhất
public UserDto signUp(UserDto registrationRequest) {
    UserDto resp = new UserDto();
    if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
        resp.setStatusCode(400);
        resp.setMessage("Email đã được đăng ký");
        return resp;
    }

    try {

        Users ourUsers = new Users();
        ourUsers.setEmail(registrationRequest.getEmail());
        ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        ourUsers.setRoles("DOCTOR"); // Đặt vai trò là DOCTOR
        ourUsers.setFullName(registrationRequest.getFullName());
        ourUsers.setBirthDate(registrationRequest.getBirthDate());
        ourUsers.setGender(registrationRequest.getGender());
        ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
        // ourUsers.setOccupation(registrationRequest.getOccupation()); // Occupation có thể là specialization của Doctor
        ourUsers.setAddress(registrationRequest.getAddress());
        ourUsers.setStatus("ACTIVE");

        // Lưu user trước để có ID
        Users savedUser = ourUserRepo.save(ourUsers);


        Doctor doctor = new Doctor();

        doctor.setUserAccount(savedUser); // Gán đối tượng Users vừa lưu vào Doctor

        doctor.setFullName(savedUser.getFullName()); // Lấy tên từ user đã lưu
        doctor.setStatus("ACTIVE"); // Có thể đặt trạng thái ban đầu

        if (registrationRequest.getOccupation() != null) {
            doctor.setSpecialization(registrationRequest.getOccupation());
        }


        doctorsRepository.save(doctor);


        if (savedUser.getId() > 0) {
            resp.setOurUsers(savedUser);
            resp.setMessage("Tài khoản bác sĩ đã được tạo thành công");
            resp.setStatusCode(200);
        }

    } catch (Exception e) {
        resp.setStatusCode(500);
        resp.setError("Lỗi trong quá trình đăng ký bác sĩ: " + e.getMessage());
        throw new RuntimeException("Đăng ký thất bại, rollback giao dịch", e);
    }

    return resp;
}
    public UserDto getUserIdsByUsersRole() {
        UserDto resp = new UserDto();
        try {
            List<Users> users = doctorRepository.findByRoles("DOCTOR");
            if (users.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No users found with role 'DOCTOR'");
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

    public Optional<Users> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<Users> findByEmail(String email) {
        // Gọi đến phương thức tương ứng của repository
        return userRepository.findByEmail(email);
    }

    public void updateUserProfile(Users updatedUserData, MultipartFile avatarFile) throws IOException, IOException {
        Long userId = updatedUserData.getId();
        if (userId == null) {
            throw new IllegalArgumentException("ID người dùng không được để trống khi cập nhật.");
        }

        // TÌM người dùng HIỆN CÓ trong database
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        // XỬ LÝ UPLOAD FILE ẢNH
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if (existingUser.getAvatarUrl() != null && !existingUser.getAvatarUrl().isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(existingUser.getAvatarUrl()));
                } catch (IOException e) {
                    System.err.println("Không thể xóa ảnh cũ: " + e.getMessage());
                }
            }

            // Tạo tên file mới, duy nhất để tránh trùng lặp
            String originalFileName = avatarFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Lưu file vào thư mục
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(avatarFile.getInputStream(), filePath);

            // Cập nhật đường dẫn ảnh mới vào đối tượng user
            // Đường dẫn này có thể được dùng trong thẻ <img>: /avatars/ten_file_moi.jpg
            existingUser.setAvatarUrl(newFileName); // <-- Chỉ lưu tên file duy nhất vào DB
            // Hoặc nếu bạn muốn lưu đường dẫn đầy đủ:

        }

        // CẬP NHẬT các trường thông tin khác từ form
        existingUser.setFullName(updatedUserData.getFullName());
        existingUser.setPhoneNumber(updatedUserData.getPhoneNumber());
        existingUser.setBirthDate(updatedUserData.getBirthDate());
        existingUser.setGender(updatedUserData.getGender());
        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setAddress(updatedUserData.getAddress());
        existingUser.setEthnicity(updatedUserData.getEthnicity());
        existingUser.setNationality(updatedUserData.getNationality());
        existingUser.setOccupation(updatedUserData.getOccupation());

        // Cập nhật các trường mới
        existingUser.setIdentityCardNumber(updatedUserData.getIdentityCardNumber());
        existingUser.setHealthInsuranceNumber(updatedUserData.getHealthInsuranceNumber());
        existingUser.setRelationship(updatedUserData.getRelationship());
        existingUser.setIssueDate(updatedUserData.getIssueDate());
        // LƯU đối tượng đã được cập nhật vào database
        userRepository.save(existingUser);
    }
}
