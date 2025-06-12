package com.clinicmanagement.config;

import com.clinicmanagement.service.JWTUtils;
import com.clinicmanagement.service.OurUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

// file: com/clinicmanagement/config/JWTAuthFIlter.java

@Component
public class JWTAuthFIlter extends OncePerRequestFilter {

    private final JWTUtils jwtUtils;
    private final OurUserDetailsService userDetailsService;
    private static final Logger logger = Logger.getLogger(JWTAuthFIlter.class.getName());

    public JWTAuthFIlter(JWTUtils jwtUtils, OurUserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // BỎ HOÀN TOÀN KHỐI IF KIỂM TRA ĐƯỜNG DẪN Ở ĐÂY.
        // BẮT ĐẦU TRỰC TIẾP TỪ VIỆC KIỂM TRA HEADER.

        // Nếu không có header hoặc header không đúng định dạng, cứ để request đi tiếp.
        // Spring Security sẽ xử lý ở bước sau dựa trên cấu hình trong SecurityConfig.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userEmail = jwtUtils.extractUsername(jwt);

            // Chỉ thiết lập Authentication nếu có userEmail và chưa có ai được xác thực trong context
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtUtils.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Đặt thông tin xác thực vào SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Có thể ghi log lỗi token ở đây nhưng không block request
            logger.log(Level.WARNING, "Cannot set user authentication: {}", e.getMessage());
        }

        // Luôn cho request đi tiếp
        filterChain.doFilter(request, response);
    }
}
