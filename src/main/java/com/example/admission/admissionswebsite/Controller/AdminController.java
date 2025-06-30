package com.example.admission.admissionswebsite.Controller;


import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller

public class AdminController {
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private AdminService adminService;

    @GetMapping("/admin")
    public String homeadmin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("email", authentication.getName());
        } else {
            model.addAttribute("email", "hello@example.com");
        }
        return "admin/index";
    }

    @GetMapping("/user")
    public String homeuser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            model.addAttribute("email", authentication.getName());
        } else {
            model.addAttribute("email", "hello@example.com");
        }
        return "user/index";
    }

    @GetMapping("/login-success")
    public String loginSuccess(RedirectAttributes redirectAttributes) {
        // Kiểm tra quyền hạn của người dùng
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"))) {
            return "redirect:/admin";  // Chuyển hướng đến /admin nếu là admin
        }
        return "redirect:/home";  // Chuyển hướng đến trang khác nếu không phải admin
    }


    @GetMapping("/admin/danh-sach-nguoi-dung")
    public String getAllUsers(Model model) {
        UserDto response = adminService.getUserIdsByUsersRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "admin/danhsachnguoidung";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "admin/danhsacnguoidung";
        }

    }







}
