package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Data
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "patient_profile_id", nullable = false)
    private PatientProfile patient;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    // Một cuộc hẹn sẽ chiếm 1 khung giờ
    @OneToOne
    @JoinColumn(name = "time_slot_id", unique = true)
    private TimeSlot timeSlot;

    private LocalDateTime createdAt;
    private LocalDateTime cancellationTime;
    private String cancellationReason;

    @Column(columnDefinition = "TEXT")
    private String reasonForVisit; // Lý do khám

    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING_CONFIRMATION, CONFIRMED, COMPLETED, CANCELLED

    @Enumerated(EnumType.STRING)
    private AppointmentType type; // ONLINE, OFFLINE

    private BigDecimal fee;
    private String paymentMethod;
    private String paymentStatus;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL)
    private MedicalRecord medicalRecord;

    public enum AppointmentStatus {
        PENDING_CONFIRMATION, CONFIRMED, COMPLETED, CANCELLED, NO_SHOW
    }

    public enum AppointmentType {
        ONLINE, OFFLINE
    }
}