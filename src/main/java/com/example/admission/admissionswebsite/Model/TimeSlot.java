package com.example.admission.admissionswebsite.Model;

import com.example.admission.admissionswebsite.Model.Doctor;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "time_slots")
@Data
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // =======================================================
    // BẮT ĐẦU SỬA ĐỔI
    // =======================================================

    // Chỉ rõ cột khóa ngoại trong CSDL là "doctor_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    // Chỉ rõ cột trong CSDL là "start_time"
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    // Chỉ rõ cột trong CSDL là "end_time"
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    // =======================================================
    // KẾT THÚC SỬA ĐỔI
    // =======================================================

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TimeSlotStatus status;

    public enum TimeSlotStatus {
        AVAILABLE, BOOKED, UNAVAILABLE
    }
}