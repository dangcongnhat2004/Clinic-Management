package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.OurUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.UUID;

@Service
public class OurUserDetailsService implements UserDetailsService {
    private static final String UPLOAD_DIR = "src/main/resources/static/avatars/";

    @Autowired
    private OurUserRepo ourUserRepo;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return ourUserRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }
    public Optional<Users> findByEmail(String email) {
        // Gọi đến phương thức tương ứng của repository
        return ourUserRepo.findByEmail(email);
    }

    public void updateUserProfile(Users updatedUserData, MultipartFile avatarFile) throws IOException, IOException {
        Long userId = updatedUserData.getId();
        if (userId == null) {
            throw new IllegalArgumentException("ID người dùng không được để trống khi cập nhật.");
        }

        // TÌM người dùng HIỆN CÓ trong database
        Users existingUser = ourUserRepo.findById(userId)
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
        ourUserRepo.save(existingUser);
    }

}
