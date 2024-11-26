package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
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

    @PostMapping("/signup")
    public String signUp(@ModelAttribute UserDto signUpRequest, Model model) {
        UserDto response = authService.signUp(signUpRequest);
        if (response.getStatusCode() == 200) {
            // Nếu đăng ký thành công, hiển thị thông báo thành công và chuyển hướng đến trang login
            model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "/home/login";  // Chuyển đến trang đăng nhập
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi và quay lại trang đăng ký
            model.addAttribute("errorMessage", response.getMessage() != null ? response.getMessage() : "Đã xảy ra lỗi.");
            return "/home/register";  // Quay lại trang đăng ký
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

            if (role.contains("ADMIN")) {
                System.out.println("Redirecting to /admin");
                return "redirect:/admin";
            } else if (role.contains("UNIVERSITY")) {
                System.out.println("Redirecting to /university");
                return "redirect:/university";
            } else if (role.contains("STUDENT")) {
                System.out.println("Redirecting to /user");
                return "redirect:/user";
            }
        } else {
            model.addAttribute("errorMessage", "Email hoặc mật khẩu không chính xác.");
            return "/home/login";
        }

        return "/home/login";
    }






    @PostMapping("/refresh")
    public ResponseEntity<UserDto> refreshToken(@RequestBody UserDto refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
}
