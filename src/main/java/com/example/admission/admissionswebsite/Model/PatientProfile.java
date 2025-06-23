package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "patient_profiles")
@Data
public class PatientProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Hồ sơ này thuộc về tài khoản User nào
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users managingUser;

    @Column(nullable = false)
    private String fullName;
    private LocalDate dateOfBirth;
    private String gender;
    private String phoneNumber;
    private String email;
    private String relationship; // SELF, FATHER, MOTHER...
    private String identityCardNumber;
    private String insuranceNumber;
    private String address;
    private String avatarUrl;

    @OneToMany(mappedBy = "patient")
    private Set<Appointment> appointments;
}