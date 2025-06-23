package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "prescription_details")
@Data
public class PrescriptionDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "prescription_id", nullable = false)
    private Prescription prescription;

    private String medicineName; // Lưu snapshot tên thuốc
    private Integer quantity;
    private String unit;
    private String dosageInstructions; // Hướng dẫn sử dụng
}