package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.repository.MajorRepository;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public Optional<Major> getMajorById(int id) {
        return majorRepository.findById(id);
    }
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

    @Transactional(readOnly = true)
    public Page<Major> getAllMajors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return majorRepository.findAll(pageable);
    }

    public MajorDto deleteMajorGroup(Integer id) {
        MajorDto response = new MajorDto();
        try {
            // Tìm nhóm ngành theo ID
            Major majorGroup = majorRepository.findById(id).orElse(null);
            if (majorGroup == null) {
                response.setStatusCode(404);
                response.setMessage("Nhóm ngành không tồn tại.");
                return response;
            }

            // Xóa bản ghi nhóm ngành
            majorRepository.delete(majorGroup);
            response.setStatusCode(200);
            response.setMessage("Xóa nhóm ngành thành công.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi xóa nhóm ngành: " + e.getMessage());
        }
        return response;
    }
    public Optional<Major> findById(Integer id) {
        return majorRepository.findById(id);
    }
    public MajorDto updateMajorGroup(MajorDto majorGroupDto) {
        MajorDto response = new MajorDto();
        try {
            // Validate dữ liệu
            if (majorGroupDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu nhóm ngành không được để trống");
                return response;
            }
            if (majorGroupDto.getMajorGroupName() == null || majorGroupDto.getMajorGroupName().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên nhóm ngành");
                return response;
            }
            if (majorGroupDto.getDescription() == null || majorGroupDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả");
                return response;
            }

            // Tìm nhóm ngành theo ID
            Optional<Major> optionalMajorGroup = majorRepository.findById(majorGroupDto.getId());
            if (!optionalMajorGroup.isPresent()) {
                response.setStatusCode(404);
                response.setMessage("Nhóm ngành không tồn tại");
                return response;
            }

            Major majorGroup = optionalMajorGroup.get();

            // Cập nhật các trường cần thiết
            majorGroup.setMajorGroupName(majorGroupDto.getMajorGroupName());
            majorGroup.setDescription(majorGroupDto.getDescription());

            // Lưu nhóm ngành đã cập nhật
            Major updatedMajorGroup = majorRepository.save(majorGroup);

            // Trả về response
            response.setStatusCode(200);
            response.setMessage("Cập nhật nhóm ngành thành công");
            response.setId(updatedMajorGroup.getId());
            response.setMajorGroupName(updatedMajorGroup.getMajorGroupName());
            response.setDescription(updatedMajorGroup.getDescription());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi cập nhật nhóm ngành: " + e.getMessage());
        }
        return response;
    }

}
