package com.example.admission.admissionswebsite.config;

import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final OurUserDetailsService ourUserDetailsService;
    private final JWTAuthFIlter jwtAuthFIlter;

    public SecurityConfig(OurUserDetailsService ourUserDetailsService, JWTAuthFIlter jwtAuthFIlter) {
        this.ourUserDetailsService = ourUserDetailsService;
        this.jwtAuthFIlter = jwtAuthFIlter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/auth/**") // Chỉ tắt CSRF cho các API xác thực
                )
                // PHÂN QUYỀN TẬP TRUNG TẠI ĐÂY
                .authorizeHttpRequests(request -> request
                        // Các URL công khai, bất kỳ ai cũng có thể truy cập
                        .requestMatchers("/", "/signup", "/auth/**", "/public/**", "/favicon.ico").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**","/manage/**","/enduser/**").permitAll()

                        // Các URL yêu cầu quyền cụ thể
                        .requestMatchers("/admin/**").hasAuthority("ADMIN") // Chỉ ADMIN được vào
                        .requestMatchers("/user/**").hasAnyAuthority("USER") // Cả hai đều được vào
                        .requestMatchers("/staff/**").hasAnyAuthority("STAFF") // Cả hai đều được vào
                        .requestMatchers("/nurse/**").hasAnyAuthority("NURSE") // Cả hai đều được vào
                        .requestMatchers("/doctor/**").hasAnyAuthority("DOCTOR") // Cả hai đều được vào

                        // Tất cả các request còn lại đều cần phải xác thực (đăng nhập)
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        // Vẫn giữ IF_REQUIRED để logout hoạt động đúng
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl("/auth/login?expired=true")
                )
                .formLogin(form -> form
                        .loginPage("/auth/login")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        // SỬ DỤNG GIẢI PHÁP "Bất khả chiến bại" để đảm bảo xóa token
                        .logoutSuccessUrl("/auth/logout-handler")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID", "jwtToken") // <-- Xóa cả cookie JWT
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/auth/login") // Chuyển về login nếu không có quyền
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFIlter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // Cấu hình này sẽ bảo Spring Security BỎ QUA hoàn toàn việc kiểm tra bảo mật
        // đối với các URL khớp với các mẫu được liệt kê.
        return (web) -> web.ignoring().requestMatchers(
                "/css/**",
                "/js/**",
                "/images/**",      // Thư mục từ upload.path
                "/uploads/**",     // Thư mục từ upload.paths
                "/Events/**",      // Thư mục từ upload.event
                "/Users/**",       // Thư mục từ upload.user
                "/Major/**",       // Thư mục từ upload.major
                "/specialty/**",   // Thư mục từ upload.specialty
                "/avatars/**"      // Thư mục từ upload.avatars
        );
    }
    // Các bean khác giữ nguyên
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(ourUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
