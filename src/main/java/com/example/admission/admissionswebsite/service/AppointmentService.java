package com.example.admission.admissionswebsite.service;


import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Service này xử lý các logic nghiệp vụ liên quan đến Appointment.
 * Không tách Interface và Implementation để đơn giản hóa.
 */
@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorsRepository doctorsRepository;
    private final PatientProfileRepository patientProfileRepository;
    private final TimeSlotRepository timeSlotRepository;
    private final OurUserRepo ourUserRepo; // <-- Repository cho Users

    @Autowired
    public AppointmentService(AppointmentRepository appointmentRepository,
                              DoctorsRepository doctorsRepository,
                              PatientProfileRepository patientProfileRepository,
                              TimeSlotRepository timeSlotRepository,
                              OurUserRepo ourUserRepo) { // <-- Thêm vào constructor
        this.appointmentRepository = appointmentRepository;
        this.doctorsRepository = doctorsRepository;
        this.patientProfileRepository = patientProfileRepository;
        this.timeSlotRepository = timeSlotRepository;
        this.ourUserRepo = ourUserRepo; // <-- Gán giá trị
    }

    @Transactional
    public Appointment createPendingAppointment(Long doctorUserId, String date, String time, Appointment.AppointmentType type, String reason, String paymentMethod) {

        // 1. Lấy thông tin bệnh nhân (Giữ nguyên)
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String currentUserEmail = userDetails.getUsername();
        PatientProfile patient = patientProfileRepository.findByUser_Email(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bệnh nhân cho người dùng: " + currentUserEmail));

        // 2. TÌM TÀI KHOẢN USER CỦA BÁC SĨ BẰNG ID ĐÃ TRUYỀN VÀO (ví dụ: 602)
        Users doctorUserAccount = ourUserRepo.findById(doctorUserId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản người dùng (bác sĩ) với ID: " + doctorUserId));

        // 3. TÌM HỒ SƠ DOCTOR TƯƠNG ỨNG VỚI TÀI KHOẢN USER ĐÓ
        Doctor doctorProfile = doctorsRepository.findByUserAccount(doctorUserAccount)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bác sĩ cho tài khoản user ID: " + doctorUserId));

        // 4. Tìm TimeSlot tương ứng
        LocalDateTime startTime = LocalDateTime.of(LocalDate.parse(date), LocalTime.parse(time));
        TimeSlot timeSlot = timeSlotRepository.findByDoctorAndStartTime(doctorProfile, startTime)
                .orElseThrow(() -> new RuntimeException("Khung giờ khám không hợp lệ hoặc đã được đặt."));

        // THÊM BƯỚC KIỂM TRA QUAN TRỌNG
        if (timeSlot.getStatus() == TimeSlot.TimeSlotStatus.BOOKED) {
            throw new RuntimeException("Lỗi: Khung giờ này vừa có người khác đặt. Vui lòng chọn lại.");
        }

        // 5. Tạo đối tượng Appointment mới
        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        // Gán đúng đối tượng Doctor (hồ sơ bác sĩ) vào cuộc hẹn
        appointment.setDoctor(doctorProfile);
        appointment.setTimeSlot(timeSlot);
        appointment.setReasonForVisit(reason);
        appointment.setType(type);
        appointment.setPaymentMethod(paymentMethod);

        // 6. CẬP NHẬT TRẠNG THÁI CỦA TIMESLOT
        timeSlot.setStatus(TimeSlot.TimeSlotStatus.BOOKED);
        timeSlotRepository.save(timeSlot);

        // 7. Thiết lập trạng thái ban đầu cho Appointment
        appointment.setStatus(Appointment.AppointmentStatus.PENDING_CONFIRMATION);
        appointment.setPaymentStatus(Appointment.PaymentStatus.UNPAID);
        appointment.setCreatedAt(LocalDateTime.now());

        // 8. Thiết lập phí khám
        appointment.setFee(doctorProfile.getExaminationFee() != null ? BigDecimal.valueOf(doctorProfile.getExaminationFee()) : new BigDecimal("300000"));

        // 9. Lưu vào CSDL và trả về
        return appointmentRepository.save(appointment);
    }
    /**
     * Xác nhận thanh toán thành công cho một cuộc hẹn.
     * Hàm này được gọi sau khi VNPay trả về kết quả thành công.
     *
     * @param appointmentId     ID của cuộc hẹn cần cập nhật.
     * @param vnpayTransactionId Mã giao dịch từ VNPay để lưu lại tham chiếu.
     */
    @Transactional
    public void confirmAppointmentPayment(Long appointmentId, String vnpayTransactionId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc hẹn với ID: " + appointmentId));

        // Cập nhật trạng thái sau khi thanh toán thành công
        appointment.setPaymentStatus(Appointment.PaymentStatus.PAID);
        appointment.setStatus(Appointment.AppointmentStatus.CONFIRMED);
        appointment.setPaymentCode(vnpayTransactionId); // Lưu lại mã giao dịch của VNPay

        appointmentRepository.save(appointment);
    }
}