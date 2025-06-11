package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "manufacturers") // Đổi tên bảng sang tiếng Anh
@Data
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String taxCode;

    private String manager; // Manager's or representative's name

    private String status; // e.g., "ACTIVE", "INACTIVE"

    private LocalDate establishmentDate; // Ngày thành lập/hoạt động

    private String businessType;

    private String registeredNationality;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(unique = true)
    private String email;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String summary; // Giới thiệu tóm tắt

    @Column(columnDefinition = "TEXT")
    private String detailedIntroduction; // Giới thiệu chi tiết

    @Column(columnDefinition = "TEXT")
    private String developmentHistory;

    @Column(columnDefinition = "TEXT")
    private String projectsAndAwards;

    /**
     * A manufacturer can produce many medicines.
     * This relationship is managed by the 'manufacturer' field in the Medicine class.
     */
    @OneToMany(mappedBy = "manufacturer", fetch = FetchType.LAZY)
    private Set<Medicine> medicines; // Đổi tên lớp và thuộc tính
}