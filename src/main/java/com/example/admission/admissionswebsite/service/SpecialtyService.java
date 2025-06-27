package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Dto.SpecialtyDto;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.Specialty;
import com.example.admission.admissionswebsite.repository.MajorRepository;
import com.example.admission.admissionswebsite.repository.SpecialtyRepository;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class SpecialtyService {
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private UniversityRepository universityRepository;
    public SpecialtyService(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    public Optional<Specialty> getSpecialById(int id) {
        return specialtyRepository.findById(id);
    }
    @Value("${upload.specialty}")
    private String uploadPath;

    public SpecialtyDto addSpecialty(SpecialtyDto specialtyDto, MultipartFile file) {
        SpecialtyDto response = new SpecialtyDto();

        try {
            // Validate đầu vào
            if (specialtyDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu chuyên khoa là bắt buộc");
                return response;
            }
            if (specialtyDto.getSpecialtyName() == null || specialtyDto.getSpecialtyName().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên chuyên khoa");
                return response;
            }
            if (specialtyDto.getDescription() == null || specialtyDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả chuyên khoa");
                return response;
            }
            if (file == null || file.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng chọn ảnh chuyên khoa");
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
            Specialty specialty = new Specialty();
            specialty.setSpecialtyName(specialty.getSpecialtyName());
            specialty.setDescription(specialty.getDescription());
            specialty.setSpecialtyImage(fileName);

            // Tìm University nếu có
//            if (majorDto.getUniversityId() != null) {
//                Optional<University> university = universityRepository.findById(majorDto.getUniversityId());
//                university.ifPresent(major::setUniversity);
//            }

            Specialty savedSpecialty = specialtyRepository.save(specialty);

            // Set response
            response.setId(savedSpecialty.getId());
            response.setSpecialtyName(savedSpecialty.getSpecialtyName());
            response.setDescription(savedSpecialty.getDescription());
            response.setStatusCode(200);
            response.setMessage("Thêm chuyên khoa thành công!");

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
    public Page<Specialty> getAllSpecialty(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return specialtyRepository.findAll(pageable);
    }

    public SpecialtyDto deleteSpecialty(Integer id) {
        SpecialtyDto response = new SpecialtyDto();
        try {
            // Tìm nhóm ngành theo ID
            Specialty specialty = specialtyRepository.findById(id).orElse(null);
            if (specialty == null) {
                response.setStatusCode(404);
                response.setMessage("Chuyên khoa không tồn tại.");
                return response;
            }

            // Xóa bản ghi nhóm ngành
            specialtyRepository.delete(specialty);
            response.setStatusCode(200);
            response.setMessage("Xóa chuyên khoa thành công.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi xóa chuyên khoa: " + e.getMessage());
        }
        return response;
    }
    public Optional<Specialty> findById(Integer id) {
        return specialtyRepository.findById(id);
    }
    public SpecialtyDto updateSpecialty(SpecialtyDto specialtyDto) {
        SpecialtyDto response = new SpecialtyDto();
        try {
            // Validate dữ liệu
            if (specialtyDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu chuyên khoa không được để trống");
                return response;
            }
            if (specialtyDto.getSpecialtyName() == null || specialtyDto.getSpecialtyName().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên nhóm ngành");
                return response;
            }
            if (specialtyDto.getDescription() == null || specialtyDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả");
                return response;
            }

            // Tìm nhóm ngành theo ID
            Optional<Specialty> optionalSpecialty = specialtyRepository.findById(specialtyDto.getId());
            if (!optionalSpecialty.isPresent()) {
                response.setStatusCode(404);
                response.setMessage("Chuyên khoa không tồn tại");
                return response;
            }

            Specialty specialty = optionalSpecialty.get();

            // Cập nhật các trường cần thiết
            specialty.setSpecialtyName(specialtyDto.getSpecialtyName());
            specialty.setDescription(specialtyDto.getDescription());

            // Lưu nhóm ngành đã cập nhật
            Specialty updatedSpecialty = specialtyRepository.save(specialty);

            // Trả về response
            response.setStatusCode(200);
            response.setMessage("Cập nhật chuyên khoa thành công");
            response.setId(updatedSpecialty.getId());
            response.setSpecialtyName(updatedSpecialty.getSpecialtyName());
            response.setDescription(updatedSpecialty.getDescription());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi cập nhật chuyên khoa: " + e.getMessage());
        }
        return response;
    }
}
