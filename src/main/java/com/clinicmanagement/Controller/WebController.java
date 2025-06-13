package com.clinicmanagement.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class WebController {

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    @GetMapping("/booking")
    public String bookingPage() {
        return "booking"; // Trả về file templates/booking.html
    }
    // Sử dụng một đường dẫn cố định cho trang chi tiết bác sĩ
    @GetMapping("/doctor-details")
    public String doctorDetailsPage() {
        return "doctor-details";
    }

    // Thêm phương thức mới
    @GetMapping("/confirmation")
    public String confirmationPage() {
        return "confirmation"; // Trả về file templates/confirmation.html
    }
} 