package com.example.admission.admissionswebsite.repository;


import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    // Thêm phương thức này để tìm một khung giờ cụ thể
    Optional<TimeSlot> findByDoctorAndStartTime(Doctor doctor, LocalDateTime startTime);
    List<TimeSlot> findByDoctorAndStartTimeBetween(Doctor doctor, LocalDateTime start, LocalDateTime end); // <-- Thêm phương thức này

}
