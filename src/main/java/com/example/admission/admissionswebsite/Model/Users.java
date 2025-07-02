package com.example.admission.admissionswebsite.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Data
@Table(name = "users")
public class Users implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fullName;
    private String email;
    private String gender;
    @JsonIgnore
    private String password;

    private String phoneNumber;
    private String birthDate;
    @Column(columnDefinition = "TEXT")
    private String address;
    private String status;
    private String roles;
    private String identityCardNumber; // Đã có: Số CCCD
    private LocalDate issueDate;

    private String occupation;
    private String hometown;
    private String nationality;
    private String ethnicity;

    // =======================================================
    // BẮT ĐẦU: CÁC TRƯỜNG MỚI ĐƯỢC THÊM
    // =======================================================
    private String healthInsuranceNumber; // Mã thẻ BHYT
    private String relationship;          // Mối quan hệ (Bản thân, Con,...)
    private String avatarUrl;             // Đường dẫn tới file ảnh avatar
    // =======================================================
    // KẾT THÚC: CÁC TRƯỜNG MỚI ĐƯỢC THÊM
    // =======================================================

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.stream(roles.split(","))
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    // ... các phương thức UserDetails khác giữ nguyên ...

    @Override
    public String getUsername() { return email; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    public enum Role {
        ADMIN,
        USER,
        DOCTOR,
        NURSE,
        STAFF
    }
}