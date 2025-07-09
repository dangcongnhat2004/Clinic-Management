package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AuthService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private AuthService authService;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/signup") // URL này nên khớp với th:action trong form
    public String processSignUp(@ModelAttribute("user") UserDto signUpRequest, Model model) {

        // Gọi service để xử lý
        UserDto response = authService.signUp(signUpRequest);

        // Kiểm tra kết quả trả về từ service
        if (response.getStatusCode() == 200) {
            // THÀNH CÔNG: Chuyển đến trang đăng nhập với thông báo
            model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "home/login";  // Render trang đăng nhập

        } else {
            // THẤT BẠI (Email trùng, hoặc lỗi khác)

            // 1. Thêm thông báo lỗi vào model
            model.addAttribute("errorMessage", response.getMessage());


            model.addAttribute("user", signUpRequest);

            // 3. Render lại trang đăng ký
            return "home/register";
        }
    }

    @PostMapping("/signin")
    public String signIn(@RequestParam String email, @RequestParam String password, Model model) {
        UserDto signInRequest = new UserDto(email, password);
        UserDto response = authService.signIn(signInRequest);


        if (response.getStatusCode() == 200) {
            String role = response.getRoles();
            String token = response.getToken();

            // Thiết lập xác thực vào SecurityContext
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    email, null, ourUserDetailsService.loadUserByUsername(email).getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);

            // Tạo cookie cho JWT
            HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
            Cookie jwtCookie = new Cookie("jwtToken", token);
            jwtCookie.setHttpOnly(true);
            jwtCookie.setPath("/");
            httpServletResponse.addCookie(jwtCookie);

            if (role.contains("STAFF")) {
                System.out.println("Redirecting to /staff");
                return "redirect:/staff";
            } else if (role.contains("DOCTOR")) {
                System.out.println("Redirecting to /doctor");
                return "redirect:/doctor";
            } else if (role.contains("NURSE")) {
                System.out.println("Redirecting to /nurse");
                return "redirect:/nurse";
            } else if (role.contains("ADMIN")) {
                System.out.println("Redirecting to /admin");
                return "redirect:/admin";
            }
            else if (role.contains("USER")) {
                System.out.println("Redirecting to /user");
                return "redirect:/user";
            }
        } else {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không chính xác.");
            return "/home/login";
        }

        return "/home/login";
    }


    @GetMapping("/auth/logout-handler")
    public String logoutHandler() {
        return "logout_handler"; // Trả về file logout_handler.html
    }



    @PostMapping("/refresh")
    public ResponseEntity<UserDto> refreshToken(@RequestBody UserDto refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
