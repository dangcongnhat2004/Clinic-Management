package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "prescriptions")
@Data
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "medical_record_id", nullable = false)
    private MedicalRecord medicalRecord;

    private LocalDate prescriptionDate;

    @Column(columnDefinition = "TEXT")
    private String doctorInstructions;

    private String attachedFileUrl; // Ảnh đơn thuốc đính kèm

    @OneToMany(mappedBy = "prescription", cascade = CascadeType.ALL)
    private Set<PrescriptionDetail> details;
}