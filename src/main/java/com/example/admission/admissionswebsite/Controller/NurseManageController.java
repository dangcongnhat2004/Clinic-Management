package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.service.DoctorManageService;
import com.example.admission.admissionswebsite.service.NurseManageService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class NurseManageController {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private NurseManageService nurseManageService;

    @GetMapping("/admin/them-y-ta")
    public String themyata(Model model) {
        model.addAttribute("user", new Users());
        return "manage/themyta";  // Trả về view admin/index.html
    }
    @PostMapping("admin/them-y-ta")
    public String signUp(@ModelAttribute UserDto signUpRequest, Model model) {
        UserDto response = nurseManageService.signUp(signUpRequest);
        if (response.getStatusCode() == 200) {
            // Nếu đăng ký thành công, hiển thị thông báo thành công và chuyển hướng đến trang login
            model.addAttribute("user", new Users());
            model.addAttribute("successMessage", "Đăng ký y tá thành công!");
            return "manage/themyta";  // Chuyển đến trang đăng nhập
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi và quay lại trang đăng ký
            model.addAttribute("errorMessage", response.getMessage() != null ? response.getMessage() : "Đã xảy ra lỗi.");
            return "manage/themyta";  // Quay lại trang đăng ký
        }
    }
    @GetMapping("/nurse")
    public String homedoctor(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("email", authentication.getName());
        } else {
            model.addAttribute("email", "hello@example.com");
        }
        return "nurse/index";
    }
    @GetMapping("/admin/danh-sach-y-ta")
    public String getAllNurse(Model model) {
        UserDto response = nurseManageService.getUserIdsByUsersRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "nurse/danhsachyta";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "nurse/danhsachyta";
        }
    }



}
