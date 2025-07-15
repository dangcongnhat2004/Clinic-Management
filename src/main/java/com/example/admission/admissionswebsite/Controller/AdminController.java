package com.example.admission.admissionswebsite.Controller;


import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.repository.AdminPostRepository;
import com.example.admission.admissionswebsite.repository.DoctorsRepository;
import com.example.admission.admissionswebsite.repository.SpecialtyRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller

public class AdminController {
     @Autowired
     private UserRepository userRepository;
     @Autowired
     private AdminService adminService;
     @Autowired
     private OurUserDetailsService userDetailsService;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private DoctorsRepository doctorRepository;
    @Autowired
    private AdminPostRepository postRepository;
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
    public String homeuser(Model model, Principal principal) {
        if (principal != null) {
            // Lấy thông tin user đang đăng nhập và đưa vào model
            String username = principal.getName();
            Users currentUser = userDetailsService.findByEmail(username).orElse(null);
            model.addAttribute("currentUser", currentUser); // <-- Dùng tên "currentUser" cho rõ ràng
            Pageable specialtyPageable = PageRequest.of(0, 6);
            List<Specialty> featuredSpecialties = specialtyRepository.findAll(specialtyPageable).getContent();
            model.addAttribute("featuredSpecialties", featuredSpecialties);

            // 2. Lấy danh sách bác sĩ nổi bật (ví dụ: 4 bác sĩ active)
            Pageable doctorPageable = PageRequest.of(0, 4);
            List<Doctor> featuredDoctors = doctorRepository.findByStatus("ACTIVE", doctorPageable).getContent();
            model.addAttribute("featuredDoctors", featuredDoctors);

            // 3. Lấy danh sách bài viết mới nhất (ví dụ: 4 bài)
            Pageable postPageable = PageRequest.of(0, 4, Sort.by("postDate").descending());
            List<AdminPost> recentPosts = postRepository.findAll(postPageable).getContent();
            model.addAttribute("recentPosts", recentPosts);
        } else {
            // Xử lý trường hợp không có ai đăng nhập
            return "redirect:/login";
        }
        return "user/homepage";
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
