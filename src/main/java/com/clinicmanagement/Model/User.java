package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import com.clinicmanagement.Model.Role;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users") // Đổi tên bảng sang tiếng Anh
@Data
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    private String fullName;

    private String status; // e.g., "ACTIVE", "INACTIVE", "BANNED"

    private LocalDate dateOfBirth;

    private String gender;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String identityCardNumber; // Số CCCD

    private LocalDate issueDate; // Ngày cấp

    private String occupation; // Nghề nghiệp

    private String hometown; // Quê quán

    private String nationality; // Quốc tịch

    private String ethnicity; // Dân tộc

    // Relationship with Role - Crucial for Spring Security
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles", // Đổi tên bảng trung gian
            joinColumns = @JoinColumn(name = "user_id"), // Đổi tên cột khóa ngoại
            inverseJoinColumns = @JoinColumn(name = "role_id") // Đổi tên cột khóa ngoại
    )
    private Set<Role> roles = new HashSet<>(); // Changed from 'role' to 'roles' to match getter/setter

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}