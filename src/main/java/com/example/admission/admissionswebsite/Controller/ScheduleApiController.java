package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.TimeSlot;
import com.example.admission.admissionswebsite.repository.DoctorsRepository;
import com.example.admission.admissionswebsite.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController

public class ScheduleApiController {
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private DoctorsRepository doctorsRepository;

    @GetMapping("/api/schedules/available-slots")
    public List<String> getAvailableSlots(@RequestParam("doctorId") Long doctorUserId, // Nhận vào User ID
                                          @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        // Tìm Doctor Profile từ User ID
        Doctor doctor = doctorsRepository.findByUserAccount_Id(doctorUserId)
                .orElse(null);

        if (doctor == null) {
            return Collections.emptyList(); // Nếu không có hồ sơ bác sĩ, trả về rỗng
        }

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<TimeSlot> slots = timeSlotRepository.findByDoctorAndStartTimeBetween(doctor, startOfDay, endOfDay);

        return slots.stream()
                .filter(slot -> slot.getStatus() == TimeSlot.TimeSlotStatus.AVAILABLE)
                .map(slot -> slot.getStartTime().toLocalTime().format(DateTimeFormatter.ofPattern("HH:mm")))
                .sorted()
                .collect(Collectors.toList());
    }
}
