package com.example.admission.admissionswebsite.config;

import com.example.admission.admissionswebsite.service.JWTUtils;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Component
public class JWTAuthFIlter extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    private static final Logger logger = Logger.getLogger(JWTAuthFIlter.class.getName());

    private static final List<String> PUBLIC_URLS = Arrays.asList(
           "/", "/signup", "/auth/**", "/public/**", "/enduser/**","/manage/**","/test","/favicon.ico"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Bỏ logic kiểm tra public URL đi, filter này sẽ chạy cho MỌI request

        final String authHeader = request.getHeader("Authorization");
        String jwtToken = null;

        // Ưu tiên lấy token từ Header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);
        } else {
            // Nếu không có trong header, thử tìm trong cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwtToken".equals(cookie.getName())) { // <-- Nên đặt tên key này vào một hằng số
                        jwtToken = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // Nếu có token, tiến hành xác thực
        if (jwtToken != null) {
            try {
                String userEmail = jwtUtils.extractUsername(jwtToken);
                // Chỉ thiết lập Authentication nếu nó chưa tồn tại
                if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

                    if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        // Thiết lập người dùng đã xác thực vào Security Context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Không nên redirect ở đây. Chỉ cần log lỗi và để request tiếp tục.
                // Spring Security sẽ tự xử lý việc từ chối truy cập ở bước sau.
//                 logger.warn("Lỗi xác thực JWT: " + e.getMessage());
            }
        }

        // Luôn luôn gọi filterChain.doFilter để request được tiếp tục xử lý
        filterChain.doFilter(request, response);
    }
    private boolean isPublicURL(String requestURI) {
        return PUBLIC_URLS.stream().anyMatch(publicUrl -> requestURI.matches(publicUrl.replace("**", ".*")));
    }
}
