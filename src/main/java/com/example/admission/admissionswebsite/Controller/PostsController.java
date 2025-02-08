package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.AdminPostDto;
import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminPostService;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PostsController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private AdminService adminService;
    @Autowired
    AdminPostService adminPostService;
    @Value("${upload.paths}")
    private String uploadPath;

    @GetMapping("/admin/them-bai-dang")
    public String addPosts() {
        return "posts/thembaidang"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }
    @PostMapping("/admin/them-bai-dang")
    public String addPost(@ModelAttribute AdminPostDto adminPostDto,
                          @RequestParam("file") MultipartFile file,
                          RedirectAttributes redirectAttributes) {
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn hình ảnh bài đăng");
            return "redirect:/admin/them-bai-dang"; // Quay lại trang thêm bài đăng
        }

        AdminPostDto response = adminPostService.addAdminPost(adminPostDto, file);
        if (response.getStatusCode() == 400) {
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            return "redirect:/admin/them-bai-dang";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thêm bài đăng thành công");
        return "redirect:/admin/them-bai-dang"; // Điều hướng đến trang quản lý
    }

    @GetMapping("/admin/danh-sach-bai-dang")
    public String getListPosts(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {

        try {
            // Đảm bảo page không bị âm
            if (page < 0) {
                page = 0;
            }
            Page<AdminPost> postPage = adminPostService.getAllAdminPosts(page, size);

            model.addAttribute("posts", postPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", postPage.getTotalPages());
            System.out.println("Danh sách bài đăng: " + postPage.getContent());

        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách bài đăng: " + e.getMessage());
        }
        return "posts/danhsachbaidang";
    }

    @PostMapping("/admin/xoa-bai-dang/{id}")
    public String deleteUniversity(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            AdminPostDto response = adminPostService.deletePosts(id);
            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa trường đại học thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa trường đại học.");
        }
        return "redirect:/admin/danh-sach-bai-dang";
    }


    @GetMapping("/admin/chinh-sua-bai-dang/{id}")
    public String hienThiFormChinhSuaBaiDang(@PathVariable Integer id, Model model) {
        AdminPost post = adminPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bài đăng với ID: " + id));
        model.addAttribute("post", post);
        return "posts/chinhsuabaidang"; // Tên view để chỉnh sửa bài đăng
    }

    @PostMapping("/admin/cap-nhat-bai-dang")
    public String capNhatBaiDang(@ModelAttribute AdminPostDto postDto, Model model) {
        AdminPostDto response = adminPostService.updatePost(postDto);

        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }

        return "redirect:/admin/danh-sach-bai-dang";
    }

}
