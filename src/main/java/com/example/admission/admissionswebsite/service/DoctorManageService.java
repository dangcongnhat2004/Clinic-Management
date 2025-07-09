package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorManageService {
    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorsRepository doctorsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository; // Thêm repo này vào
    @Value("${upload.event}")
    private String uploadPath;
    private static final String UPLOAD_DIR = "src/main/resources/static/avatars/";

//    public UserDto signUp(UserDto registrationRequest){
//        UserDto resp = new UserDto();
//        if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
//            resp.setStatusCode(400);
//            resp.setMessage("Email already registered");
//            return resp;
//        }
//
//        try {
//            Users ourUsers = new Users();
//            ourUsers.setEmail(registrationRequest.getEmail());
//            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
//            ourUsers.setRoles("DOCTOR");
//            ourUsers.setFullName(registrationRequest.getFullName());
//            ourUsers.setBirthDate(registrationRequest.getBirthDate());
//            ourUsers.setGender(registrationRequest.getGender());
//            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
//            ourUsers.setOccupation(registrationRequest.getOccupation());
//            ourUsers.setAddress(registrationRequest.getAddress());
//            ourUsers.setStatus("ACTIVE");
//            Users ourUserResult = ourUserRepo.save(ourUsers);
//
//            if (ourUserResult != null && ourUserResult.getId()>0) {
//                resp.setOurUsers(ourUserResult);
//                resp.setMessage("User Saved Successfully");
//                resp.setStatusCode(200);
//            }
//        }catch (Exception e) {
//            resp.setStatusCode(500);
//            resp.setError("Error during user registration: " + e.getMessage());
//        }
//
//        return resp;
//    }
@Transactional // Thêm annotation này để đảm bảo cả 2 thao tác lưu là một giao dịch duy nhất
public UserDto signUp(UserDto registrationRequest) {
    UserDto resp = new UserDto();
    if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
        resp.setStatusCode(400);
        resp.setMessage("Email đã được đăng ký");
        return resp;
    }

    try {

        Users ourUsers = new Users();
        ourUsers.setEmail(registrationRequest.getEmail());
        ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        ourUsers.setRoles("DOCTOR"); // Đặt vai trò là DOCTOR
        ourUsers.setFullName(registrationRequest.getFullName());
        ourUsers.setBirthDate(registrationRequest.getBirthDate());
        ourUsers.setGender(registrationRequest.getGender());
        ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
        // ourUsers.setOccupation(registrationRequest.getOccupation()); // Occupation có thể là specialization của Doctor
        ourUsers.setAddress(registrationRequest.getAddress());
        ourUsers.setStatus("ACTIVE");

        // Lưu user trước để có ID
        Users savedUser = ourUserRepo.save(ourUsers);


        Doctor doctor = new Doctor();

        doctor.setUserAccount(savedUser); // Gán đối tượng Users vừa lưu vào Doctor

        doctor.setFullName(savedUser.getFullName()); // Lấy tên từ user đã lưu
        doctor.setStatus("ACTIVE"); // Có thể đặt trạng thái ban đầu

        if (registrationRequest.getOccupation() != null) {
            doctor.setSpecialization(registrationRequest.getOccupation());
        }


        doctorsRepository.save(doctor);


        if (savedUser.getId() > 0) {
            resp.setOurUsers(savedUser);
            resp.setMessage("Tài khoản bác sĩ đã được tạo thành công");
            resp.setStatusCode(200);
        }

    } catch (Exception e) {
        resp.setStatusCode(500);
        resp.setError("Lỗi trong quá trình đăng ký bác sĩ: " + e.getMessage());
        throw new RuntimeException("Đăng ký thất bại, rollback giao dịch", e);
    }

    return resp;
}
    public UserDto getUserIdsByUsersRole() {
        UserDto resp = new UserDto();
        try {
            List<Users> users = doctorRepository.findByRoles("DOCTOR");
            if (users.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No users found with role 'DOCTOR'");
                return resp;
            }
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(
                            Math.toIntExact(user.getId()),
                            user.getFullName(),
                            user.getEmail(),
                            user.getAddress(),
                            user.getBirthDate(),
                            user.getOccupation(),
                            user.getPhoneNumber()

                    ))
                    .collect(Collectors.toList());
            resp.setStatusCode(200);
            resp.setMessage("User IDs retrieved successfully");
            resp.setOurUser(userDtos); // Đảm bảo trả về danh sách UserDto
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Error while retrieving user IDs: " + e.getMessage());
        }
        return resp;
    }

    public Optional<Users> findById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<Users> findByEmail(String email) {
        // Gọi đến phương thức tương ứng của repository
        return userRepository.findByEmail(email);
    }

    public void updateUserProfile(Users updatedUserData, MultipartFile avatarFile) throws IOException, IOException {
        Long userId = updatedUserData.getId();
        if (userId == null) {
            throw new IllegalArgumentException("ID người dùng không được để trống khi cập nhật.");
        }

        // TÌM người dùng HIỆN CÓ trong database
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng với ID: " + userId));

        // XỬ LÝ UPLOAD FILE ẢNH
        if (avatarFile != null && !avatarFile.isEmpty()) {
            // Tạo thư mục nếu chưa tồn tại
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            if (existingUser.getAvatarUrl() != null && !existingUser.getAvatarUrl().isEmpty()) {
                try {
                    Files.deleteIfExists(Paths.get(existingUser.getAvatarUrl()));
                } catch (IOException e) {
                    System.err.println("Không thể xóa ảnh cũ: " + e.getMessage());
                }
            }

            // Tạo tên file mới, duy nhất để tránh trùng lặp
            String originalFileName = avatarFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            String newFileName = UUID.randomUUID().toString() + fileExtension;

            // Lưu file vào thư mục
            Path filePath = uploadPath.resolve(newFileName);
            Files.copy(avatarFile.getInputStream(), filePath);

            // Cập nhật đường dẫn ảnh mới vào đối tượng user
            // Đường dẫn này có thể được dùng trong thẻ <img>: /avatars/ten_file_moi.jpg
            existingUser.setAvatarUrl(newFileName); // <-- Chỉ lưu tên file duy nhất vào DB
            // Hoặc nếu bạn muốn lưu đường dẫn đầy đủ:

        }

        // CẬP NHẬT các trường thông tin khác từ form
        existingUser.setFullName(updatedUserData.getFullName());
        existingUser.setPhoneNumber(updatedUserData.getPhoneNumber());
        existingUser.setBirthDate(updatedUserData.getBirthDate());
        existingUser.setGender(updatedUserData.getGender());
        existingUser.setEmail(updatedUserData.getEmail());
        existingUser.setAddress(updatedUserData.getAddress());
        existingUser.setEthnicity(updatedUserData.getEthnicity());
        existingUser.setNationality(updatedUserData.getNationality());
        existingUser.setOccupation(updatedUserData.getOccupation());

        // Cập nhật các trường mới
        existingUser.setIdentityCardNumber(updatedUserData.getIdentityCardNumber());
        existingUser.setHealthInsuranceNumber(updatedUserData.getHealthInsuranceNumber());
        existingUser.setRelationship(updatedUserData.getRelationship());
        existingUser.setIssueDate(updatedUserData.getIssueDate());
        // LƯU đối tượng đã được cập nhật vào database
        userRepository.save(existingUser);
    }


    @Transactional
    public List<Appointment> getAppointmentsByDoctorEmail(String email) {

        Users doctorUser = ourUserRepo.findByEmail(email)
                .orElse(null);

        if (doctorUser == null) {
            return Collections.emptyList();
        }

        Doctor doctorProfile = doctorsRepository.findByUserAccount(doctorUser)
                .orElse(null); // Trả về null nếu không có hồ sơ doctor

        if (doctorProfile == null) {
            return Collections.emptyList(); // Nếu không có hồ sơ doctor, không có lịch hẹn
        }


        List<Appointment> appointments = appointmentRepository.findByDoctor(doctorProfile);

        // (Tùy chọn) Sắp xếp lại danh sách
        appointments.sort(Comparator.comparing(a -> a.getTimeSlot().getStartTime()));

        return appointments;
    }

    @Transactional
    public void cancelAppointment(Long appointmentId, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn với ID: " + appointmentId));

        if (appointment.getStatus() == Appointment.AppointmentStatus.COMPLETED ||
                appointment.getStatus() == Appointment.AppointmentStatus.CANCELLED) {
            throw new IllegalStateException("Không thể hủy một lịch hẹn đã hoàn thành hoặc đã bị hủy.");
        }

        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointment.setCancellationReason(reason);
        appointment.setCancellationTime(LocalDateTime.now());

        // TODO: (Nâng cao) Giải phóng TimeSlot nếu cần


        appointmentRepository.save(appointment);

        // TODO: Gửi email/thông báo cho bệnh nhân về việc lịch hẹn đã bị hủy.
    }

    // Lấy các TimeSlot của bác sĩ trong ngày hôm nay
    public List<TimeSlot> getTodayTimeSlots(String doctorEmail) {
        Doctor doctor = findDoctorByEmail(doctorEmail);
        LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
        LocalDateTime endOfDay = LocalDate.now().atTime(23, 59, 59);
        return timeSlotRepository.findByDoctorAndStartTimeBetween(doctor, startOfDay, endOfDay);
    }

    // Tạo TimeSlot hàng loạt
    @Transactional
    public void createTimeSlotsForDoctor(String doctorEmail, String workDateStr, String startTimeStr, String endTimeStr, int durationInMinutes) {
        Doctor doctor = findDoctorByEmail(doctorEmail);

        LocalDate workDate = LocalDate.parse(workDateStr);
        LocalTime startTime = LocalTime.parse(startTimeStr);
        LocalTime endTime = LocalTime.parse(endTimeStr);

        if (startTime.isAfter(endTime)) {
            throw new IllegalArgumentException("Giờ bắt đầu không thể sau giờ kết thúc.");
        }

        List<TimeSlot> newSlots = new ArrayList<>();
        LocalDateTime currentSlotTime = LocalDateTime.of(workDate, startTime);

        while (!currentSlotTime.toLocalTime().isAfter(endTime.minusMinutes(durationInMinutes))) {
            LocalDateTime endSlotTime = currentSlotTime.plusMinutes(durationInMinutes);

            TimeSlot newSlot = new TimeSlot();
            newSlot.setDoctor(doctor);
            newSlot.setStartTime(currentSlotTime);
            newSlot.setEndTime(endSlotTime);
            newSlot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);

            newSlots.add(newSlot);

            currentSlotTime = endSlotTime;
        }

        // TODO: Kiểm tra xem các slot này có bị trùng với slot đã có không trước khi lưu

        timeSlotRepository.saveAll(newSlots);
    }

    // Hàm tiện ích để tìm bác sĩ
    private Doctor findDoctorByEmail(String email) {
        Users user = ourUserRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        return doctorsRepository.findByUserAccount(user).orElseThrow(() -> new RuntimeException("Doctor profile not found"));
    }
    public List<TimeSlot> getTimeSlotsForDate(String doctorEmail, LocalDate date) {
        Doctor doctor = findDoctorByEmail(doctorEmail);
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        return timeSlotRepository.findByDoctorAndStartTimeBetween(doctor, startOfDay, endOfDay);
    }
}
