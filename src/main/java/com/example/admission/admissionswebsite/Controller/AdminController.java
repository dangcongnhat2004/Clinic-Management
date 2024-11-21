package com.example.admission.admissionswebsite.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String homeadmin() {
        // Kiểm tra Authentication khi xử lý yêu cầu trong controller
        System.out.println("Current Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("Authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        return "admin/index";
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


    @GetMapping("/admin/danh-sach-nganh")
    public String danhsachnganh() {
        return "admin/danhsachnganh";
    }
    @GetMapping("/admin/danh-sach-su-kien")
    public String danhsachsukien() {
        return "admin/danhsachsukien";
    }
    @GetMapping("/admin/danh-sach-bai-dang")
    public String danhsachbaidang() {
        return "admin/danhsachbaidang";
    }

    @GetMapping("admin/danh-sach-truong-dai-hoc")
    public String danhsachtruongdaihoc() {
        return "admin/danhsachtruongdaihoc";
    }
    @GetMapping("admin/them-nganh")
    public String themnganh() {
        return "admin/themnganh";
    }
}
