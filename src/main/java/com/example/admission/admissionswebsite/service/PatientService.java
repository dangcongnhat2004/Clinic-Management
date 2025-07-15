package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Model.Appointment;
import com.example.admission.admissionswebsite.Model.MedicalRecord;
import com.example.admission.admissionswebsite.Model.PatientProfile;
import com.example.admission.admissionswebsite.Model.TimeSlot;
import com.example.admission.admissionswebsite.repository.AppointmentRepository;
import com.example.admission.admissionswebsite.repository.MedicalRecordRepository;
import com.example.admission.admissionswebsite.repository.PatientProfileRepository;
import com.example.admission.admissionswebsite.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
public class PatientService {
    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientProfileRepository patientProfileRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository; // Cần repo này để giải phóng slot

    @Autowired
    public PatientService(MedicalRecordRepository medicalRecordRepository, PatientProfileRepository patientProfileRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.patientProfileRepository = patientProfileRepository;
    }

    // Lấy danh sách bệnh án theo email của người dùng (bệnh nhân)
    @Transactional(readOnly = true)
    public List<MedicalRecord> getMedicalRecordsByPatientEmail(String email) {
        // 1. Tìm hồ sơ bệnh nhân (PatientProfile) từ email của người dùng
        PatientProfile patientProfile = patientProfileRepository.findByUser_Email(email)
                .orElse(null);

        if (patientProfile == null) {
            // Nếu không có hồ sơ bệnh nhân, không thể có bệnh án
            return Collections.emptyList();
        }

        // 2. Gọi phương thức repository để lấy danh sách bệnh án
        // Sắp xếp theo ID giảm dần để kết quả mới nhất lên trên
        return medicalRecordRepository.findByAppointment_PatientOrderByIdDesc(patientProfile);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, String patientEmail, String reason) {
        // 1. Tìm cuộc hẹn
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn với ID: " + appointmentId));

        // 2. (Bảo mật) Kiểm tra xem người dùng đang đăng nhập có đúng là chủ của cuộc hẹn này không
        if (!appointment.getPatient().getUser().getEmail().equals(patientEmail)) {
            throw new SecurityException("Bạn không có quyền hủy lịch hẹn này.");
        }

        // 3. Kiểm tra xem cuộc hẹn có thể hủy được không
        if (appointment.getStatus() != Appointment.AppointmentStatus.CONFIRMED) {
            throw new IllegalStateException("Chỉ có thể hủy các lịch hẹn đã được xác nhận.");
        }
        if (appointment.getTimeSlot().getStartTime().isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("Không thể hủy một lịch hẹn đã qua.");
        }

        // 4. Cập nhật thông tin hủy cho Appointment
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason != null ? reason : "Bệnh nhân tự hủy.");
        appointment.setCancellationTime(LocalDateTime.now());

        // 5. Giải phóng TimeSlot để người khác có thể đặt
        TimeSlot timeSlot = appointment.getTimeSlot();
        timeSlot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);
        timeSlotRepository.save(timeSlot);

        // 6. Lưu lại thay đổi cho Appointment
        appointmentRepository.save(appointment);

        // TODO: Gửi email/thông báo cho bác sĩ về việc lịch hẹn đã bị hủy.
    }
}
