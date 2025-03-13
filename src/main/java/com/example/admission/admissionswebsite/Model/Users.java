package com.example.admission.admissionswebsite.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
    private String highSchoolName;
    private String email;
    private String gender;
    @JsonIgnore
    private String password;

    private String phoneNumber;
    private String birthDate;
    private String address;
    private String status;
    private String roles; // Cột roles lưu trữ dưới dạng chuỗi

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Chuyển đổi chuỗi roles thành các quyền (authorities)
        return Arrays.stream(roles.split(","))
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email; // Assuming email is used as the username
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

    public enum Role {
        ADMIN,
        STUDENT,
        UNIVERSITY
    }
}
