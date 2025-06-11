package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "roles") // Đổi tên bảng sang tiếng Anh
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    private String name; // e.g., "ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"
}