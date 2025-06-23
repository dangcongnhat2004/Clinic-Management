package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.AuthService;
import com.example.admission.admissionswebsite.service.ManageService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ManageController {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private ManageService manageService;

    @GetMapping("/admin/them-bac-si")
    public String thembacsi(Model model) {
        model.addAttribute("user", new Users());
        return "manage/thembacsi";  // Trả về view admin/index.html
    }
    @PostMapping("/them-bac-si")
    public String signUp(@ModelAttribute UserDto signUpRequest, Model model) {
        UserDto response = manageService.signUp(signUpRequest);
        if (response.getStatusCode() == 200) {
            // Nếu đăng ký thành công, hiển thị thông báo thành công và chuyển hướng đến trang login
            model.addAttribute("successMessage", "Đăng ký bác sĩ thành công!");
            return "redirect:/admin";  // Chuyển đến trang đăng nhập
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi và quay lại trang đăng ký
            model.addAttribute("errorMessage", response.getMessage() != null ? response.getMessage() : "Đã xảy ra lỗi.");
            return "redirect:/admin";  // Quay lại trang đăng ký
        }
    }

}
