package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.repository.AppointmentRepository;
import com.example.admission.admissionswebsite.repository.DoctorsRepository;
import com.example.admission.admissionswebsite.repository.PatientProfileRepository;
import com.example.admission.admissionswebsite.repository.TimeSlotRepository;
import com.example.admission.admissionswebsite.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class UserController {
    @Autowired
    private AdminPostService adminPostService;
    @Autowired
    private EnduserService enduserService;
    @Autowired
    private EventService eventService;
    @Autowired
    private DoctorManageService doctorManageService;
    @Autowired
    private OurUserDetailsService userDetailsService;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private PatientProfileRepository patientProfileRepository;
    @Autowired
    private DoctorsRepository doctorsRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    @GetMapping("/auth/signup")
    public String showSignUpForm(Model model) {
        model.addAttribute("user", new Users());

        return "/home/register";
    }
    @GetMapping("/auth/login")
    public String loginPage(@ModelAttribute("successMessage") String successMessage, Model model) {
        model.addAttribute("successMessage", successMessage);
        return "/home/login"; // This will render login.html
    }
//    @GetMapping("/")
//    public String homePage() {
//        return "/user/home";
//    }


    @GetMapping("/")
    public String homePage(Model model) {
        List<University> universities = enduserService.getAllUniversities();
        model.addAttribute("universities", universities);
//        List<Major> majors = enduserService.getAllMajor();
//        model.addAttribute("majors", majors);
        List<Specialty> specialty = enduserService.getAllSpecialty();
        model.addAttribute("specialty", specialty);
        List<Users> doctors  = enduserService.getAllDoctor();
        model.addAttribute("doctors",doctors);
        List<AdminPost> posts = enduserService.getAllPost();
        model.addAttribute("posts", posts);

//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/home"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }
    @GetMapping("/danh-sach-nhom-nganh")
    public String listMajors(Model model,@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "8") int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            Page<Major> majorPage = enduserService.getAllMajors(page, size);
            model.addAttribute("majors", majorPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", majorPage.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách ngành học: " + e.getMessage());
        }
//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/listmajor"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }
    @GetMapping("/user/thong-tin-ca-nhan")
    public String showUserProfile(Principal principal, Model model) {
        // 1. Kiểm tra xem người dùng đã đăng nhập chưa
        if (principal == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang login
            return "redirect:/auth/login";
        }

        // 2. Lấy username của người dùng đang đăng nhập
        String username = principal.getName();

        // 3. Dùng username để tìm thông tin đầy đủ trong database
        Users loggedInUser = userDetailsService.findByEmail(username) // Hoặc findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng: " + username));

        model.addAttribute("information", loggedInUser);

        return "user/profile";
    }
    @PostMapping("/user/thong-tin-ca-nhan/cap-nhat")
    public String updateProfile(@ModelAttribute("information") Users updatedUserData,
                                @RequestParam("avatarFile") MultipartFile avatarFile, // <-- THÊM THAM SỐ NÀY
                                RedirectAttributes redirectAttributes) {
        try {
            // Gọi service để thực hiện việc cập nhật, truyền cả file vào
            userDetailsService.updateUserProfile(updatedUserData, avatarFile);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace(); // In lỗi ra console để debug
        }

        return "redirect:/user/thong-tin-ca-nhan";
    }
    @GetMapping("/danh-sach-truong-dai-hoc")
    public String listUniversity(Model model,@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "8") int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            Page<University> universityPage = enduserService.getAllUniversity(page, size);
            model.addAttribute("universities", universityPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", universityPage.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách ngành học: " + e.getMessage());
        }
//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/listuniversity"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }
    @GetMapping("/danh-sach-su-kien")
    public String listEvent(Model model,@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "8") int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            Page<Event> events = eventService.getAllEvents(page, size);
            model.addAttribute("events", events.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", events.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách ngành học: " + e.getMessage());
        }
//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/listevent"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }
    @GetMapping("/danh-sach-tin-tuc")
    public String listAdmissionPost(Model model,@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "6") int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            Page<AdminPost> admissionPage = enduserService.getAllAdmissionPost(page, size);
            model.addAttribute("admission", admissionPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", admissionPage.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách ngành học: " + e.getMessage());
        }
//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/listadmissionpost"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }

    @GetMapping("/chi-tiet-tin-tuc/{id}")
    public String showNewDetails(@PathVariable Integer id, Model model) {
        AdminPost adminPost = adminPostService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id));
        model.addAttribute("adminPost", adminPost);
        return "/user/postdetail";
    }
    @GetMapping("/user/dat-lich")
    public String appointments(Model model) {
//        UserDto response = doctorManageService.getUserIdsByUsersRole();
//        if (response.getStatusCode() == 200) {
//            model.addAttribute("usersList", response.getOurUser());
//            return "/user/appointments"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
//
//        } else {
//            model.addAttribute("errorMessage", response.getMessage());
//            return "404";
//        }
        List<Specialty> specialty = enduserService.getAllSpecialty();
        model.addAttribute("specialty", specialty);
        List<Doctor> doctors  = enduserService.getAllDoctorsAsDoctorObjects();
        model.addAttribute("doctors",doctors);
//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/appointments";
    }
    // CÁCH VIẾT AN TOÀN HƠN
    @GetMapping("/user/dat-lich/chon-lich")
    public String showScheduleSelectionPage(
            // 1. Nhận vào ID của USER, và phải là kiểu Long
            @RequestParam("doctorId") Long doctorUserId,
            // 2. Nhận vào ngày đã chọn (không bắt buộc)
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
            Model model) {

        // 3. TÌM HỒ SƠ BÁC SĨ (Doctor) DỰA TRÊN ID TÀI KHOẢN (Users)
        Doctor doctorProfile = doctorsRepository.findByUserAccount_Id(doctorUserId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bác sĩ cho tài khoản user ID: " + doctorUserId));

        // 4. TRUYỀN ĐỐI TƯỢNG DOCTOR (chứa đầy đủ thông tin) SANG VIEW
        model.addAttribute("doctor", doctorProfile);

        // 5. NẾU người dùng đã chọn một ngày, TÌM CÁC KHUNG GIỜ TRỐNG
        if (selectedDate != null) {
            LocalDateTime startOfDay = selectedDate.atStartOfDay();
            LocalDateTime endOfDay = selectedDate.atTime(23, 59, 59);

            // Tìm các TimeSlot hợp lệ trong CSDL
            List<TimeSlot> availableSlots = timeSlotRepository
                    .findByDoctorAndStartTimeBetween(doctorProfile, startOfDay, endOfDay)
                    .stream()
                    .filter(slot -> slot.getStatus() == TimeSlot.TimeSlotStatus.AVAILABLE)
                    .sorted(Comparator.comparing(TimeSlot::getStartTime))
                    .collect(Collectors.toList());

            model.addAttribute("availableSlots", availableSlots);
            model.addAttribute("selectedDateValue", selectedDate.toString()); // Truyền ngày dạng YYYY-MM-DD để giữ giá trị
        } else {
            // Nếu chưa chọn ngày, truyền danh sách rỗng để view xử lý
            model.addAttribute("availableSlots", Collections.emptyList());
        }

        return "/user/appointmenttwo"; // Trả về trang chọn lịch hẹn
    }
    @GetMapping("/user/dat-lich/nhap-thong-tin")
    public String showInfoEntryPage(
            @RequestParam("doctorId") Long doctorUserId, // Đây là ID của User
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            Model model) {

        // Tìm hồ sơ bác sĩ để hiển thị thông tin
        Doctor doctorProfile = doctorsRepository.findByUserAccount_Id(doctorUserId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bác sĩ cho tài khoản user ID: " + doctorUserId));

        // Truyền tất cả thông tin cần thiết sang cho view của Trang 3
        model.addAttribute("doctor", doctorProfile);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedTime", time);

        // Trả về tên file HTML của Trang 3
        return "/user/appointmentthree"; // <-- Đảm bảo bạn có file tên là "appointmentthree.html" trong thư mục /templates/user/
    }
    // TRANG 3: Hiển thị trang xác nhận
    // CÁCH VIẾT CÓ THỂ GÂY LỖI
    // CÁCH VIẾT AN TOÀN HƠN
    @GetMapping("/user/dat-lich/xac-nhan")
    public String showConfirmationPage(@RequestParam("doctorId") Integer doctorId,
                                       @RequestParam(value = "date", required = false) String date, // Cho phép có thể null để kiểm tra
                                       @RequestParam(value = "time", required = false) String time, // Cho phép có thể null để kiểm tra
                                       Model model) {

        Users selectedDoctor = doctorManageService.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: " + doctorId));

        // 2. Kiểm tra ngày tháng và thời gian (rất quan trọng)
        if (date == null || date.isEmpty() || time == null || time.isEmpty()) {
            throw new IllegalArgumentException("Ngày hoặc giờ khám không được để trống.");
        }

        // 3. Nếu mọi thứ hợp lệ, thêm vào model
        model.addAttribute("doctor", selectedDoctor);
        model.addAttribute("selectedDate", date);
        model.addAttribute("selectedTime", time);

        // 4. Trả về view
        return "/user/appointmentthree"; // Đây là trang để xác nhận lịch hẹn
    }

    @PostMapping("/user/dat-lich/hoan-tat")
    public String completeBooking(@RequestParam("doctorId") Integer doctorId,
                                  @RequestParam("appointmentDate") String date,
                                  @RequestParam("appointmentTime") String time,
                                  @RequestParam("appointmentType") String type,
                                  @RequestParam("reasonForVisit") String reason,
                                  RedirectAttributes redirectAttributes) {

        // TODO: Lấy thông tin người dùng đang đăng nhập
        // User currentUser = ... ;

        // TODO: Tạo một đối tượng Appointment và lưu vào CSDL
        // Appointment appointment = new Appointment();
        // appointment.setDoctorId(doctorId);
        // appointment.setUserId(currentUser.getId());
        // appointment.setDate(LocalDate.parse(date));
        // ...
        // appointmentService.save(appointment);

        // Gửi thông báo thành công về trang kết quả
        redirectAttributes.addFlashAttribute("successMessage", "Bạn đã đặt lịch khám thành công!");

        // Chuyển hướng đến trang lịch sử đặt khám hoặc trang chủ
        return "redirect:/user/lich-su-dat-kham";
    }
    // THÊM MỚI: CONTROLLER CHO BƯỚC 4 (THANH TOÁN)
    @GetMapping("/user/dat-lich/thanh-toan")
    public String showPaymentPage(
            // Sửa Lỗi 1: Dùng Long cho doctorId
            @RequestParam("doctorId") Long doctorUserId,
            @RequestParam("appointmentDate") String date, // <-- CONTROLLER ĐANG YÊU CẦU THAM SỐ TÊN LÀ "appointmentDate"
            @RequestParam("appointmentTime") String time,
            @RequestParam("appointmentType") Appointment.AppointmentType appointmentType,
            @RequestParam("reasonForVisit") String reason,
            Model model) {

        // =======================================================
        // BẮT ĐẦU SỬA LỖI LOGIC
        // =======================================================

        // Bước 1: Tìm HỒ SƠ BÁC SĨ (Doctor) dựa trên ID TÀI KHOẢN (Users)
        // Giả sử bạn có phương thức này trong DoctorRepository
        Doctor doctorProfile = doctorsRepository.findByUserAccount_Id(doctorUserId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bác sĩ cho tài khoản user ID: " + doctorUserId));

        // Bước 2: TRUYỀN ĐÚNG ĐỐI TƯỢNG DOCTOR VÀO MODEL
        // Sửa Lỗi 4: Giờ đây biến 'doctor' trong view sẽ là một đối tượng Doctor
        model.addAttribute("doctor", doctorProfile);

        // =======================================================
        // KẾT THÚC SỬA LỖI LOGIC
        // =======================================================

        // Gửi các thông tin khác đã thu thập được sang view
        model.addAttribute("appointmentDate", date);
        model.addAttribute("appointmentTime", time);
        model.addAttribute("appointmentType", appointmentType);
        model.addAttribute("reasonForVisit", reason);

        return "/user/appointmentfour"; // Trả về view của bước 4
    }
    @GetMapping("/user/lich-su-dat-kham")
    public String showAppointmentHistory(Model model, RedirectAttributes redirectAttributes) {
        try {
            // 1. Lấy thông tin người dùng đang đăng nhập
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String currentUserEmail = userDetails.getUsername();

            // 2. Tìm tất cả hồ sơ bệnh nhân của người dùng này
            List<PatientProfile> patientProfiles = patientProfileRepository.findAllByUser_Email(currentUserEmail);

            if (patientProfiles.isEmpty()) {
                // Nếu người dùng không có hồ sơ nào, hiển thị danh sách rỗng
                model.addAttribute("appointments", Collections.emptyList());
                return "user/appointment_history"; // Tên file view
            }

            // 3. Lấy tất cả các cuộc hẹn từ các hồ sơ đó và sắp xếp
            List<Appointment> appointments = appointmentRepository.findByPatientInOrderByTimeSlot_StartTimeDesc(patientProfiles);

            model.addAttribute("appointments", appointments);

        } catch (Exception e) {
            // Xử lý trường hợp người dùng chưa đăng nhập hoặc có lỗi
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để xem lịch sử đặt hẹn.");
            return "redirect:/auth/login";
        }

        return "user/appointment_history"; // Trả về file HTML
    }
    // Nhận dữ liệu từ form ở Bước 4
//    @PostMapping("/dat-lich/hoan-tat")
//    public String completeBooking(@RequestParam("doctorId") Long doctorId,
//                                  @RequestParam("appointmentDate") String date,
//                                  @RequestParam("appointmentTime") String time,
//                                  @RequestParam("appointmentType") String type,
//                                  @RequestParam("reasonForVisit") String reason,
//                                  @RequestParam("paymentMethod") String paymentMethod, // Nhận thêm phương thức thanh toán
//                                  RedirectAttributes redirectAttributes) {
//
//        // TODO: Xử lý logic lưu vào CSDL
//        // ...
//
//        // TODO: Xử lý logic gọi API thanh toán dựa trên `paymentMethod`
//        // ...
//
//        redirectAttributes.addFlashAttribute("successMessage", "Bạn đã đặt lịch và thanh toán thành công!");
//        return "redirect:/lich-su-dat-kham";
//    }

//    @GetMapping("/lich-su-dat-kham")
//    public String showAppointmentHistory(Model model, RedirectAttributes redirectAttributes) {
//        try {
//            // 1. Lấy thông tin người dùng đang đăng nhập
//            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//            String currentUserEmail = userDetails.getUsername();
//
//            // 2. Tìm tất cả hồ sơ bệnh nhân của người dùng này
//            // (Trong trường hợp một tài khoản có thể quản lý nhiều hồ sơ)
//            List<PatientProfile> patientProfiles = patientProfileRepository.findAllByUser_Email(currentUserEmail);
//
//            if (patientProfiles.isEmpty()) {
//                model.addAttribute("appointments", Collections.emptyList());
//                return "user/appointment-history";
//            }
//
//            // 3. Lấy tất cả các cuộc hẹn từ các hồ sơ đó
//            List<Appointment> appointments = appointmentRepository.findByPatientInOrderByTimeSlot_StartTimeDesc(patientProfiles);
//
//            model.addAttribute("appointments", appointments);
//
//        } catch (Exception e) {
//            // Xử lý trường hợp người dùng chưa đăng nhập hoặc có lỗi
//            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng đăng nhập để xem lịch sử đặt hẹn.");
//            return "redirect:/auth/login";
//        }
//
//        return "user/appointment-history"; // Trả về file HTML
//    }
    @GetMapping("/user/thong-tin-ca-nhan/{id}")
    public String profile(Model model) {

//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/profile"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
    }
    @GetMapping("/user/course")
    public String course() {
        return "user/course";
    }
    @GetMapping("/user/course/detail")
    public String courseDetail() {
        return "course/detail";
    }

    @GetMapping("/user/course/java")
    public String courseJoin() {
        return "user/";
    }

    @GetMapping("/tai-lieu")
    public String tailieu() {
        return "user/tailieu";
    }

    @GetMapping("/dat-lich-buoc-hai")
    public String college() {
        return "user/appointmenttwo";
    }
    @GetMapping("/user/event")
    public String event() {
        return "eventdetail";
    }

    @GetMapping("/user/search")
    public String search() {
        return "user/search";
    }
    @GetMapping("/user/list-course")
    public String listCourse() {
        return "user/listcourse";
    }

    @GetMapping("/user/list-major")
    public String listMajor() {
        return "user/listmajor";
    }

    @GetMapping("/user/mapservice")
    public String mapService() {
        return "user/mapservice";
    }

}
