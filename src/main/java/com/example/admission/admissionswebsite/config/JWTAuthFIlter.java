package com.example.admission.admissionswebsite.config;

import com.example.admission.admissionswebsite.service.JWTUtils;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.logging.Logger;

@Component
public class JWTAuthFIlter  extends OncePerRequestFilter {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private OurUserDetailsService ourUserDetailsService;

    private static final Logger logger = Logger.getLogger(JWTAuthFIlter.class.getName());

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;

        // Kiểm tra xem header có chứa Bearer token không
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Lấy JWT token từ header Authorization
        jwtToken = authHeader.substring(7); // Lấy phần token sau "Bearer "
        userEmail = jwtUtils.extractUsername(jwtToken);

        // Kiểm tra xem người dùng đã xác thực hay chưa
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Tải thông tin người dùng từ DB
            UserDetails userDetails = ourUserDetailsService.loadUserByUsername(userEmail);

            // Kiểm tra token hợp lệ
            if (jwtUtils.isTokenValid(jwtToken, userDetails)) {
                // Tạo token xác thực
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Ghi log thông tin quyền của người dùng
                logger.info("User authorities: " + userDetails.getAuthorities());
                System.out.println("User authorities from JWTAuthFilter: " + userDetails.getAuthorities());

                // Đặt thông tin xác thực vào SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authToken);
                // Kiểm tra quyền trong filter
                System.out.println("Authentication set in filter: " + SecurityContextHolder.getContext().getAuthentication());
                System.out.println("Authorities in filter: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());
            } else {
                // Nếu token không hợp lệ hoặc đã hết hạn, ghi log và trả về lỗi
                logger.warning("Token không hợp lệ hoặc đã hết hạn.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Mã lỗi 401
                response.getWriter().write("Token không hợp lệ hoặc đã hết hạn.");
                return;
            }
        }

        // Tiến hành lọc request tiếp theo trong chuỗi filter
        filterChain.doFilter(request, response);
    }

}
