package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.service.DoctorManageService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import com.example.admission.admissionswebsite.service.StaffManageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StaffManageController {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private StaffManageService staffManageService;

    @GetMapping("/admin/them-phuc-vu")
    public String them(Model model) {
        model.addAttribute("user", new Users());
        return "manage/themphucvu";  // Trả về view admin/index.html
    }
    @PostMapping("admin/them-phuc-vu")
    public String signUp(@ModelAttribute UserDto signUpRequest, Model model) {
        UserDto response = staffManageService.signUp(signUpRequest);
        if (response.getStatusCode() == 200) {
            // Nếu đăng ký thành công, hiển thị thông báo thành công và chuyển hướng đến trang login
            model.addAttribute("user", new Users());
            model.addAttribute("successMessage", "Đăng ký phục vụ thành công!");
            return "manage/themphucvu";  // Chuyển đến trang đăng nhập
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi và quay lại trang đăng ký
            model.addAttribute("errorMessage", response.getMessage() != null ? response.getMessage() : "Đã xảy ra lỗi.");
            return "manage/themphucvu";  // Quay lại trang đăng ký
        }
    }
    @GetMapping("/staff")
    public String homedoctor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("email", authentication.getName());
        } else {
            model.addAttribute("email", "hello@example.com");
        }
        return "staff/index";
    }

    @GetMapping("/admin/danh-sach-phuc-vu")
    public String getAllStaff(Model model) {
        UserDto response = staffManageService.getUserIdsByUsersRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "staff/danhsachphucvu";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "staff/danhsachphucvu";
        }

    }
}
