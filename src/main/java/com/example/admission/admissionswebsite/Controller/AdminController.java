package com.example.admission.admissionswebsite.Controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    @GetMapping("/admin")
    public String homeadmin() {
        return "admin/index";
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
