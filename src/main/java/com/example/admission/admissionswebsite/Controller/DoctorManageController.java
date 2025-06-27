package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.DoctorManageService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class DoctorManageController {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private DoctorManageService doctorManageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private AdminService adminService;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/admin/them-bac-si")
    public String thembacsi(Model model) {
        model.addAttribute("user", new Users());
        return "manage/thembacsi";  // Trả về view admin/index.html
    }
    @PostMapping("admin/them-bac-si")
    public String signUp(@ModelAttribute UserDto signUpRequest, Model model) {
        UserDto response = doctorManageService.signUp(signUpRequest);
        if (response.getStatusCode() == 200) {
            // Nếu đăng ký thành công, hiển thị thông báo thành công và chuyển hướng đến trang login
            model.addAttribute("user", new Users());
            model.addAttribute("successMessage", "Đăng ký bác sĩ thành công!");
            return "manage/thembacsi";  // Chuyển đến trang đăng nhập
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi và quay lại trang đăng ký
            model.addAttribute("errorMessage", response.getMessage() != null ? response.getMessage() : "Đã xảy ra lỗi.");
            return "manage/thembacsi";  // Quay lại trang đăng ký
        }
    }

    @GetMapping("/doctor")
    public String homedoctor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("email", authentication.getName());
        } else {
            model.addAttribute("email", "hello@example.com");
        }
        return "doctor/index";
    }

    @GetMapping("/admin/danh-sach-bac-si")
    public String getAllDoctor(Model model) {
        UserDto response = doctorManageService.getUserIdsByUsersRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "doctor/danhsachbacsi";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "doctor/danhsacbacsi";
        }

    }

//    @GetMapping("/admin/them-truong-dai-hoc")
//    public String getUserIds(Model model) {
//
//        UserDto response = adminService.getUserIdsByUniversityRole();
//        if (response.getStatusCode() == 200) {
//            model.addAttribute("usersList", response.getOurUser());
//            return "admin/themtruong";
//        } else {
//            model.addAttribute("errorMessage", response.getMessage());
//            return "404";
//        }
//    }

    @GetMapping("/chi-tiet-bac-si/{id}")
    public String showDoctorDetail(@PathVariable Integer id, Model model) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id));
        model.addAttribute("university", university);
        return "/user/universitydetail";
    }

//    @PostMapping("/admin/them-truong-dai-hoc")
//    public String addUniversity(@ModelAttribute UniversityDto universityDto,
//                                @RequestParam("usersId") Integer userId,
//                                RedirectAttributes redirectAttributes,
//                                Model model) {
//
//        try {
//            universityDto.setUserId(userId);
//            UniversityDto response = universityService.addUniversity(universityDto, universityDto.getUniversityLogo());
//
//            if (response.getStatusCode() == 200) {
//                redirectAttributes.addFlashAttribute("successMessage", "Thêm trường thành công!");
//                return "redirect:/admin/them-truong-dai-hoc";
//            } else {
//                model.addAttribute("errorMessage", response.getMessage());
//                return "admin/themtruong";
//            }
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Đã xảy ra lỗi khi thêm trường. Vui lòng thử lại.");
//            return "admin/themtruong";
//        }
//    }




    // Hiển thị danh sách các trường đại học
//    @GetMapping("/admin/danh-sach-bac-si")
//    public String getAllDoctors(Model model) {
//        List<University> universities = universityService.getAllUniversities();
//        model.addAttribute("universities", universities);
//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
//        return "admin/danhsachtruongdaihoc"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
//    }


    // Phương thức để xóa trường đại học

//    @PostMapping("/admin/xoa-truong-dai-hoc/{id}")
//    public String deleteUniversity(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            UniversityDto response = universityService.deleteUniversity(id);
//            if (response.getStatusCode() == 200) {
//                redirectAttributes.addFlashAttribute("successMessage", "Xóa trường đại học thành công!");
//            } else {
//                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
//            }
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa trường đại học.");
//        }
//        return "redirect:/admin/danh-sach-truong-dai-hoc";
//    }

//    @PostMapping("/admin/chinh-sua-truong-dai-hoc/{id}")
//    public String updateUniversity(@PathVariable Integer id, @ModelAttribute UniversityDto universityDto,
//                                 @RequestParam("usersId") Integer userId,
//                                 RedirectAttributes redirectAttributes,
//                                 Model model) {
//
//    }

    @GetMapping("/admin/chinh-sua-bac-si/{id}")
    public String hienThiFormChinhSua(@PathVariable Integer id, Model model) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trường với ID: " + id));
        model.addAttribute("university", university);
        return "admin/chinhsuatruongdaihoc";
    }
    @PostMapping("/admin/cap-nhat-bac-si")
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
