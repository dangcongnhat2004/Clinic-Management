package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "vital_signs")
@Data
public class VitalSign {
    @Id
    private Long id; // Dùng chung ID với MedicalRecord

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private MedicalRecord medicalRecord;

    private String bloodPressure;
    private Double heartRate;
    private Double temperature;
    private Double respiratoryRate;
    private Double height;
    private Double weight;
    private Double bmi;
}