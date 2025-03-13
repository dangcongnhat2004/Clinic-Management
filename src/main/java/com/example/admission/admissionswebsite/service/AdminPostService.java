package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.AdminPostDto;
import com.example.admission.admissionswebsite.Model.AdminPost;

import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.repository.AdminPostRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class AdminPostService {
    @Autowired
    private AdminPostRepository adminPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.paths}")
    private String uploadPath;

    public AdminPostDto addAdminPost(AdminPostDto adminPostDto, MultipartFile file) {
        AdminPostDto response = new AdminPostDto();
        try {
            if (adminPostDto == null) {
                response.setStatusCode(400);
                response.setMessage("Post data is required");
                return response;
            }

            if (adminPostDto.getTitle() == null || adminPostDto.getTitle().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tiêu đề bài đăng");
                return response;
            }
            if (adminPostDto.getDescription() == null || adminPostDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả bài đăng");
                return response;
            }
            if (adminPostDto.getContent() == null || adminPostDto.getContent().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập nội dung bài đăng");
                return response;
            }
            if (adminPostDto.getOwnerName() == null || adminPostDto.getOwnerName().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mã trường của bài đăng");
                return response;
            }
            // Validate file
            if (file == null || file.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng chọn hình ảnh bài đăng");
                return response;
            }

            // Save the file
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadDirPath = Paths.get(uploadPath); // Ensure 'uploadPath' is defined elsewhere
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }
            Path filePath = uploadDirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            // Create and save AdminPost
            AdminPost adminPost = new AdminPost();
            adminPost.setTitle(adminPostDto.getTitle());
            adminPost.setDescription(adminPostDto.getDescription());
            adminPost.setContent(adminPostDto.getContent());
            adminPost.setOwnerName(adminPostDto.getOwnerName());
            adminPost.setImage(fileName); // Save file name in the database
            adminPost.setPostDate(new Date());
            AdminPost savedAdminPost = adminPostRepository.save(adminPost);

            // Set response
            response.setStatusCode(200);
            response.setMessage("Thêm bài đăng thành công");
            response.setTitle(savedAdminPost.getTitle());
            response.setDescription(savedAdminPost.getDescription());
            response.setContent(savedAdminPost.getContent());
            response.setPostDate(savedAdminPost.getPostDate());
        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Error during file upload: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi thêm bài đăng: " + e.getMessage());
        }
        return response;
    }


    @Transactional(readOnly = true)
    public Page<AdminPost> getAllAdminPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminPostRepository.findAll(pageable);
    }
    public AdminPostDto deletePosts(Integer id) {
        AdminPostDto response = new AdminPostDto();
        try {
            // Tìm trường đại học theo ID
            AdminPost adminPost = adminPostRepository.findById(id).orElse(null);
            if (adminPost == null) {
                response.setStatusCode(404);
                response.setMessage("Trường đại học không tồn tại.");
                return response;
            }

            // Xóa ảnh logo nếu tồn tại
            String imagePath = uploadPath + "/" + adminPost.getImage();
            File imageFile = new File(imagePath);
            if (imageFile.exists() && !imageFile.delete()) {
                response.setStatusCode(500);
                response.setMessage("Không thể xóa ảnh của trường đại học.");
                return response;
            }

            // Xóa bản ghi trường đại học
            adminPostRepository.delete(adminPost);
            response.setStatusCode(200);
            response.setMessage("Xóa trường đại học thành công.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi xóa trường đại học: " + e.getMessage());
        }
        return response;
    }

    public Optional<AdminPost> findById(Integer id) {
        return adminPostRepository.findById(id);
    }

    public AdminPostDto updatePost(AdminPostDto postDto) {
        AdminPostDto response = new AdminPostDto();
        try {
            // Validate dữ liệu
            if (postDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu bài đăng không được để trống");
                return response;
            }
            if (postDto.getTitle() == null || postDto.getTitle().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tiêu đề");
                return response;
            }
            if (postDto.getDescription() == null || postDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả");
                return response;
            }
            if (postDto.getContent() == null || postDto.getContent().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập nội dung");
                return response;
            }
            if (postDto.getOwnerName() == null || postDto.getOwnerName().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mã trường của bài đăng");
                return response;
            }
            // Tìm bài đăng theo ID
            Optional<AdminPost> optionalPost = adminPostRepository.findById(postDto.getId());
            if (!optionalPost.isPresent()) {
                response.setStatusCode(404);
                response.setMessage("Bài đăng không tồn tại");
                return response;
            }

            AdminPost post = optionalPost.get();

            // Cập nhật các trường cần thiết (ngoại trừ ảnh)
            post.setTitle(postDto.getTitle());
            post.setOwnerName(postDto.getOwnerName());
            post.setDescription(postDto.getDescription());
            post.setContent(postDto.getContent());

            // Lưu bài đăng đã cập nhật
            AdminPost updatedPost = adminPostRepository.save(post);

            // Trả về response
            response.setStatusCode(200);
            response.setMessage("Cập nhật bài đăng thành công");
            response.setId(updatedPost.getId());
            response.setTitle(updatedPost.getTitle());
            response.setDescription(updatedPost.getDescription());
            response.setContent(updatedPost.getContent());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi cập nhật bài đăng: " + e.getMessage());
        }
        return response;
    }


    private String saveImage(MultipartFile imageFile) throws IOException {
        // Đường dẫn thư mục lưu trữ ảnh
        String uploadDir = "uploads/";

        // Tạo thư mục nếu chưa tồn tại
        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Lấy tên ảnh
        String imageName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        String filePath = uploadDir + imageName;

        // Lưu ảnh vào thư mục
        Files.copy(imageFile.getInputStream(), Paths.get(filePath));

        return filePath;  // Trả về đường dẫn lưu ảnh
    }

}
