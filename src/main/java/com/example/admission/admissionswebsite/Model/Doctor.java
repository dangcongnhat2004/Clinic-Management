package com.example.admission.admissionswebsite.Model;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết One-to-One với tài khoản User để bác sĩ có thể đăng nhập
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users userAccount;

    @Column(nullable = false)
    private String fullName;

    private String degree; // Bằng cấp, học vị: BSCKII, Ths.BS...

    private String avatarUrl;

    private String specialization;

    private String status;

    @Column(columnDefinition = "TEXT")
    private String biography; // Tiểu sử

    @Column(columnDefinition = "TEXT")
    private String experience; // Kinh nghiệm làm việc

    private Double examinationFee; // Phí khám

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;
}
