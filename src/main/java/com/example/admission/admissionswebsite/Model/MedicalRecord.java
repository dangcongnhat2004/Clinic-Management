package com.example.admission.admissionswebsite.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "medical_records")
@Data
public class MedicalRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false, unique = true)
    private Appointment appointment;

    @Column(columnDefinition = "TEXT")
    private String diagnosis; // Chẩn đoán

    @Column(columnDefinition = "TEXT")
    private String conclusion; // Kết luận

    @Column(columnDefinition = "TEXT")
    private String doctorNotes;

    // Các mối quan hệ với các thành phần khác của hồ sơ
    @OneToOne(mappedBy = "medicalRecord", cascade = CascadeType.ALL)
    private VitalSign vitalSign;
}
