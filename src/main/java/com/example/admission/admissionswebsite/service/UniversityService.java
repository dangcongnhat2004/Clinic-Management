    package com.example.admission.admissionswebsite.service;

    import com.example.admission.admissionswebsite.Dto.UniversityDto;
    import com.example.admission.admissionswebsite.Model.University;
    import com.example.admission.admissionswebsite.Model.Users;
    import com.example.admission.admissionswebsite.repository.UniversityRepository;
    import com.example.admission.admissionswebsite.repository.UserRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.beans.factory.annotation.Value;
    import org.springframework.stereotype.Service;
    import org.springframework.web.multipart.MultipartFile;
    import java.io.File;
    import java.util.Optional;

    import java.io.IOException;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import java.util.List;

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
                // Validate universityDto
                if (universityDto == null) {
                    response.setStatusCode(400);
                    response.setMessage("University data is required");
                    return response;
                }

                // Validate required fields
                if (universityDto.getNameSchool() == null || universityDto.getNameSchool().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập tên trường");
                    return response;
                }

                if (universityDto.getUniCode() == null || universityDto.getUniCode().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập mã trường");
                    return response;
                }

                if (universityDto.getAddress() == null || universityDto.getAddress().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập địa chỉ");
                    return response;
                }

                if (universityDto.getDescription() == null || universityDto.getDescription().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập mô tả");
                    return response;
                }

                // Validate file
                if (file == null || file.isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng chọn logo");
                    return response;
                }

                // Validate userId
                if (universityDto.getUserId() == null) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng chọn người dùng");
                    return response;
                }

                // Retrieve the user linked to the university
                Users user = userRepository.findById(universityDto.getUserId()).orElse(null);
                if (user == null) {
                    response.setStatusCode(404);
                    response.setMessage("User không tồn tại");
                    return response;
                }

                // Tạo và lưu University
                University university = new University();
                university.setUniCode(universityDto.getUniCode());
                university.setNameSchool(universityDto.getNameSchool());
                university.setAddress(universityDto.getAddress());
                university.setDescription(universityDto.getDescription());

                // Save the logo file
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                Path uploadDirPath = Paths.get(uploadPath);
                if (!Files.exists(uploadDirPath)) {
                    Files.createDirectories(uploadDirPath);
                }
                Path filePath = uploadDirPath.resolve(fileName);
                Files.copy(file.getInputStream(), filePath);

                university.setUniversityLogo(fileName);
                university.setUsers(user);

                University savedUniversity = universityRepository.save(university);

                // Set response
                response.setStatusCode(200);
                response.setMessage("University added successfully");
                response.setUniCode(university.getUniCode());
                response.setId(savedUniversity.getId());
                response.setNameSchool(savedUniversity.getNameSchool());
                response.setAddress(savedUniversity.getAddress());
                response.setDescription(savedUniversity.getDescription());

            } catch (IOException e) {
                response.setStatusCode(500);
                response.setMessage("Error during file upload: " + e.getMessage());
            } catch (Exception e) {
                response.setStatusCode(500);
                response.setMessage("Error during university registration: " + e.getMessage());
            }
            return response;
        }


        public List<University> getAllUniversities() {
            return universityRepository.findAll();
        }

        public UniversityDto deleteUniversity(Integer id) {
            UniversityDto response = new UniversityDto();
            try {
                // Tìm trường đại học theo ID
                University university = universityRepository.findById(id).orElse(null);
                if (university == null) {
                    response.setStatusCode(404);
                    response.setMessage("Trường đại học không tồn tại.");
                    return response;
                }

                // Xóa ảnh logo nếu tồn tại
                String imagePath = uploadPath + "/" + university.getUniversityLogo();
                File imageFile = new File(imagePath);
                if (imageFile.exists() && !imageFile.delete()) {
                    response.setStatusCode(500);
                    response.setMessage("Không thể xóa ảnh của trường đại học.");
                    return response;
                }

                // Xóa bản ghi trường đại học
                universityRepository.delete(university);
                response.setStatusCode(200);
                response.setMessage("Xóa trường đại học thành công.");
            } catch (Exception e) {
                response.setStatusCode(500);
                response.setMessage("Đã xảy ra lỗi khi xóa trường đại học: " + e.getMessage());
            }
            return response;
        }

        public Optional<University> findById(Integer id) {
            return universityRepository.findById(id);
        }

        public UniversityDto updateUniversity(UniversityDto universityDto) {
            UniversityDto response = new UniversityDto();
            try {
                // Validate universityDto
                if (universityDto == null) {
                    response.setStatusCode(400);
                    response.setMessage("University data is required");
                    return response;
                }

                // Validate required fields
                if (universityDto.getNameSchool() == null || universityDto.getNameSchool().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập tên trường");
                    return response;
                }
                if (universityDto.getUniCode() == null || universityDto.getUniCode().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập mã trường");
                    return response;
                }
                if (universityDto.getAddress() == null || universityDto.getAddress().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập địa chỉ");
                    return response;
                }
                if (universityDto.getDescription() == null || universityDto.getDescription().isEmpty()) {
                    response.setStatusCode(400);
                    response.setMessage("Vui lòng nhập mô tả");
                    return response;
                }
    //            if (universityDto.getUserId() == null || universityDto.getUserId().equals("")) {
    //                response.setStatusCode(400);
    //                response.setMessage("Vui lòng chọn người dùng tương ứng");
    //                return response;
    //            }
                // Find the university by ID
                Optional<University> optionalUniversity = universityRepository.findById(universityDto.getId());
                if (!optionalUniversity.isPresent()) {
                    response.setStatusCode(404);
                    response.setMessage("Trường đại học không tồn tại");
                    return response;
                }

                University university = optionalUniversity.get();
                // Update university fields
                university.setNameSchool(universityDto.getNameSchool());
                university.setUniCode(universityDto.getUniCode());
                university.setAddress(universityDto.getAddress());
                university.setDescription(universityDto.getDescription());

                // Save the updated university
                University updatedUniversity = universityRepository.save(university);

                // Set response
                response.setStatusCode(200);
                response.setMessage("Cập nhật trường đại học thành công");
                response.setId(updatedUniversity.getId());
                response.setNameSchool(updatedUniversity.getNameSchool());
                response.setUniCode(updatedUniversity.getUniCode());
                response.setAddress(updatedUniversity.getAddress());
                response.setDescription(updatedUniversity.getDescription());
            } catch (Exception e) {
                response.setStatusCode(500);
                response.setMessage("Đã xảy ra lỗi khi cập nhật trường đại học: " + e.getMessage());
            }
            return response;
        }

    }