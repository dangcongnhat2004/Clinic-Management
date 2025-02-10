package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.repository.MajorRepository;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class MajorService {

    @Autowired
    private MajorRepository majorRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Value("${upload.major}")
    private String uploadPath;

    public MajorDto addMajor(MajorDto majorDto, MultipartFile file) {
        MajorDto response = new MajorDto();

        try {
            // Validate đầu vào
            if (majorDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu nhóm ngành là bắt buộc");
                return response;
            }
            if (majorDto.getMajorGroupName() == null || majorDto.getMajorGroupName().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên nhóm ngành");
                return response;
            }
            if (majorDto.getDescription() == null || majorDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả nhóm ngành");
                return response;
            }
            if (file == null || file.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng chọn ảnh nhóm ngành");
                return response;
            }

            // Lưu file ảnh
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadDirPath = Paths.get(uploadPath);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }
            Path filePath = uploadDirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Tạo Major
            Major major = new Major();
            major.setMajorGroupName(majorDto.getMajorGroupName());
            major.setDescription(majorDto.getDescription());
            major.setMajorImage(fileName);

            // Tìm University nếu có
//            if (majorDto.getUniversityId() != null) {
//                Optional<University> university = universityRepository.findById(majorDto.getUniversityId());
//                university.ifPresent(major::setUniversity);
//            }

            Major savedMajor = majorRepository.save(major);

            // Set response
            response.setId(savedMajor.getId());
            response.setMajorGroupName(savedMajor.getMajorGroupName());
            response.setDescription(savedMajor.getDescription());
            response.setStatusCode(200);
            response.setMessage("Thêm nhóm ngành thành công!");

        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Lỗi khi lưu ảnh: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi thêm nhóm ngành: " + e.getMessage());
        }

        return response;
    }
}
