package com.example.admission.admissionswebsite.Controller;


import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class UniveristyController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private AdminService adminService;
    @Value("${upload.path}")
    private String uploadPath;
    // Hiển thị form thêm trường đại học
//    @GetMapping("/them-truong-dai-hoc")
//    public String themtruongdaihoc(Model model) {
//        List<Users> usersList = userRepository.findAll();
//        model.addAttribute("usersList", usersList);
//        model.addAttribute("university", new University());
//        return "admin/themtruong";
//    }
    @GetMapping("/admin/them-truong-dai-hoc")
    public String getUserIds(Model model) {

        UserDto response = adminService.getUserIdsByUniversityRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "admin/themtruong";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "404";
        }
    }

    @GetMapping("/chi-tiet-truong-dai-hoc/{id}")
    public String showUniversityDetail(@PathVariable Integer id, Model model) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id));
        model.addAttribute("university", university);
        return "/user/universitydetail";
    }

    @PostMapping("/admin/them-truong-dai-hoc")
    public String addUniversity(@ModelAttribute UniversityDto universityDto,
                                @RequestParam("usersId") Integer userId,
                                RedirectAttributes redirectAttributes,
                                Model model) {

        try {
            universityDto.setUserId(userId);
            UniversityDto response = universityService.addUniversity(universityDto, universityDto.getUniversityLogo());

            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", "Thêm trường thành công!");
                return "redirect:/admin/them-truong-dai-hoc";
            } else {
                model.addAttribute("errorMessage", response.getMessage());
                return "admin/themtruong";
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi thêm trường. Vui lòng thử lại.");
            return "admin/themtruong";
        }
    }




    // Hiển thị danh sách các trường đại học
    @GetMapping("/admin/danh-sach-truong-dai-hoc")
    public String getAllUniversities(Model model) {
        List<University> universities = universityService.getAllUniversities();
        model.addAttribute("universities", universities);
        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "admin/danhsachtruongdaihoc"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }


    // Phương thức để xóa trường đại học

    @PostMapping("/admin/xoa-truong-dai-hoc/{id}")
    public String deleteUniversity(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            UniversityDto response = universityService.deleteUniversity(id);
            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa trường đại học thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa trường đại học.");
        }
        return "redirect:/admin/danh-sach-truong-dai-hoc";
    }

//    @PostMapping("/admin/chinh-sua-truong-dai-hoc/{id}")
//    public String updateUniversity(@PathVariable Integer id, @ModelAttribute UniversityDto universityDto,
//                                 @RequestParam("usersId") Integer userId,
//                                 RedirectAttributes redirectAttributes,
//                                 Model model) {
//
//    }

    @GetMapping("/admin/chinh-sua-truong-dai-hoc/{id}")
    public String hienThiFormChinhSua(@PathVariable Integer id, Model model) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trường với ID: " + id));
        model.addAttribute("university", university);
        return "admin/chinhsuatruongdaihoc";
    }
    @PostMapping("/admin/cap-nhat-truong-dai-hoc")
    public String updateUniversity(@ModelAttribute UniversityDto universityDto, Model model) {
        UniversityDto response = universityService.updateUniversity(universityDto);

        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }

        // Trả về trang admin/update-university sau khi xử lý
        return "redirect:/admin/danh-sach-truong-dai-hoc";
    }


}