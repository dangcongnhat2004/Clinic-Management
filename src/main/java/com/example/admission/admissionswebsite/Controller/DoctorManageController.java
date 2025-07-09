package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.repository.AppointmentRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.DoctorManageService;
import com.example.admission.admissionswebsite.service.OurUserDetailsService;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DoctorManageController {
    @Autowired
    private OurUserDetailsService ourUserDetailsService;
    @Autowired
    private DoctorManageService doctorManageService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private AdminService adminService;
    @Value("${upload.path}")
    private String uploadPath;
    @GetMapping("/admin/them-bac-si")
    public String thembacsi(Model model) {
        model.addAttribute("user", new Users());
        return "manage/thembacsi";  // Trả về view admin/index.html
    }
    @PostMapping("admin/them-bac-si")
    public String signUp(@ModelAttribute UserDto signUpRequest, Model model) {
        UserDto response = doctorManageService.signUp(signUpRequest);
        if (response.getStatusCode() == 200) {
            // Nếu đăng ký thành công, hiển thị thông báo thành công và chuyển hướng đến trang login
            model.addAttribute("user", new Users());
            model.addAttribute("successMessage", "Đăng ký bác sĩ thành công!");
            return "manage/thembacsi";  // Chuyển đến trang đăng nhập
        } else {
            // Nếu có lỗi, hiển thị thông báo lỗi và quay lại trang đăng ký
            model.addAttribute("errorMessage", response.getMessage() != null ? response.getMessage() : "Đã xảy ra lỗi.");
            return "manage/thembacsi";  // Quay lại trang đăng ký
        }
    }

    @GetMapping("/doctor")
    public String homedoctor(Model model, Principal principal) { // <-- Thêm Principal
        if (principal != null) {
            // Lấy thông tin user đang đăng nhập và đưa vào model
            String username = principal.getName();
            Users currentUser = doctorManageService.findByEmail(username).orElse(null);
            model.addAttribute("currentUser", currentUser); // <-- Dùng tên "currentUser" cho rõ ràng
        } else {
            // Xử lý trường hợp không có ai đăng nhập
            return "redirect:/login";
        }



        return "doctor/index";
    }
    @GetMapping("/doctor/thong-tin-ca-nhan")
    public String showMyProfile(Principal principal, Model model) {
        // 1. Kiểm tra xem người dùng đã đăng nhập chưa
        if (principal == null) {
            // Nếu chưa đăng nhập, chuyển hướng về trang login
            return "redirect:/login";
        }

        // 2. Lấy username của người dùng đang đăng nhập
        String username = principal.getName();

        // 3. Dùng username để tìm thông tin đầy đủ trong database
        Users loggedInUser = doctorManageService.findByEmail(username) // Hoặc findByUsername(username)
                .orElseThrow(() -> new IllegalStateException("Không tìm thấy người dùng: " + username));

        model.addAttribute("information", loggedInUser);

        return "doctor/profile";
    }

    @GetMapping("/admin/danh-sach-bac-si")
    public String getAllDoctor(Model model) {
        UserDto response = doctorManageService.getUserIdsByUsersRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "doctor/danhsachbacsi";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "doctor/danhsacbacsi";
        }

    }

//    @GetMapping("/admin/them-truong-dai-hoc")
//    public String getUserIds(Model model) {
//
//        UserDto response = adminService.getUserIdsByUniversityRole();
//        if (response.getStatusCode() == 200) {
//            model.addAttribute("usersList", response.getOurUser());
//            return "admin/themtruong";
//        } else {
//            model.addAttribute("errorMessage", response.getMessage());
//            return "404";
//        }
//    }

    @GetMapping("/chi-tiet-bac-si/{id}")
    public String showDoctorDetail(@PathVariable Integer id, Model model) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id));
        model.addAttribute("university", university);
        return "/user/universitydetail";
    }
    @PostMapping("/doctor/thong-tin-ca-nhan/cap-nhat")
    public String updateProfile(@ModelAttribute("information") Users updatedUserData,
                                @RequestParam("avatarFile") MultipartFile avatarFile, // <-- THÊM THAM SỐ NÀY
                                RedirectAttributes redirectAttributes) {
        try {
            // Gọi service để thực hiện việc cập nhật, truyền cả file vào
            doctorManageService.updateUserProfile(updatedUserData, avatarFile);

            redirectAttributes.addFlashAttribute("successMessage", "Cập nhật thông tin cá nhân thành công!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi: " + e.getMessage());
            e.printStackTrace(); // In lỗi ra console để debug
        }

        return "redirect:/doctor/thong-tin-ca-nhan";
    }


    @GetMapping("/doctor/schedule")
    public String viewMySchedule(Model model, @AuthenticationPrincipal UserDetails userDetails) {


        if (userDetails == null) {
            // Nếu không có người dùng đăng nhập, không thể tiếp tục.
            // Ghi log để biết vấn đề và chuyển hướng về trang đăng nhập.
            System.out.println("!!! LỖI: Không thể lấy thông tin người dùng từ Security Context. UserDetails is null.");
            return "redirect:/auth/login?error=session_expired";
        }

        String doctorEmail = userDetails.getUsername();
        System.out.println("--- BẮT ĐẦU DEBUG: Lấy lịch cho bác sĩ email: " + doctorEmail);

        List<Appointment> appointments = doctorManageService.getAppointmentsByDoctorEmail(doctorEmail);

        if (appointments != null) {
            System.out.println("--- KẾT QUẢ DEBUG: Tìm thấy " + appointments.size() + " cuộc hẹn.");
        } else {
            System.out.println("--- KẾT QUẢ DEBUG: Danh sách cuộc hẹn là NULL.");
        }

        model.addAttribute("appointments", appointments);
        return "doctor/scheduleDoctor"; // Đảm bảo tên file view đúng
    }
    @GetMapping("/doctor/appointment/{id}")
    public String viewAppointmentDetails(@PathVariable("id") Long appointmentId, Model model, @AuthenticationPrincipal UserDetails userDetails) {

        // 1. Tìm cuộc hẹn bằng ID
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lịch hẹn với ID: " + appointmentId));

        // 2. (Bảo mật) Kiểm tra xem bác sĩ đang đăng nhập có đúng là bác sĩ của cuộc hẹn này không
        String currentDoctorEmail = userDetails.getUsername();
        if (!appointment.getDoctor().getUserAccount().getEmail().equals(currentDoctorEmail)) {
            return "404";
        }

        // 4. Gửi đối tượng appointment vào model
        model.addAttribute("appointment", appointment);

        return "doctor/appointment-details";
    }
    // Sửa lại phương thức GET
    @GetMapping("/doctor/working-hours")
    public String viewWorkingHours(@RequestParam(name = "date", required = false) String date, // Nhận tham số ngày từ URL
                                   Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {

        LocalDate selectedDate;
        if (date != null && !date.isEmpty()) {
            selectedDate = LocalDate.parse(date);
        } else {
            selectedDate = LocalDate.now(); // Mặc định là ngày hôm nay nếu không có tham số
        }

        // Lấy các TimeSlot của ngày đã chọn
        List<TimeSlot> timeSlotsForSelectedDate = doctorManageService.getTimeSlotsForDate(userDetails.getUsername(), selectedDate);

        model.addAttribute("timeSlots", timeSlotsForSelectedDate); // Đổi tên biến cho chung chung
        model.addAttribute("selectedDate", selectedDate.toString()); // Gửi lại ngày đã chọn để hiển thị trên input

        return "doctor/working-hours";
    }
    @GetMapping("/doctor/tat-ca-thoi-gian")
    public String showScheduleSelectionPage(@RequestParam("doctorId") Integer doctorId,
                                            // Thêm tham số ngày, không bắt buộc
                                            @RequestParam(name = "selectedDate", required = false) String selectedDateStr,
                                            Model model) {

        // 1. Lấy thông tin bác sĩ (giữ nguyên)
        Users doctor = doctorManageService.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ bác sĩ với ID: " + doctorId));
        model.addAttribute("doctor", doctor);

        // 2. Xử lý ngày và lấy các khung giờ
        List<TimeSlot> availableSlots = new ArrayList<>();
        LocalDate dateToQuery;

        if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
            // Nếu có tham số ngày được truyền vào từ URL
            dateToQuery = LocalDate.parse(selectedDateStr);
            // TODO: Viết một phương thức trong service để lấy các slot trống theo bác sĩ và ngày
            // availableSlots = timeSlotService.getAvailableSlotsForDoctorOnDate(doctor, dateToQuery);
        } else {
            // Mặc định, có thể không hiển thị gì hoặc hiển thị của ngày hôm nay
            dateToQuery = LocalDate.now();
            // availableSlots = timeSlotService.getAvailableSlotsForDoctorOnDate(doctor, dateToQuery);
        }

        // ----- DỮ LIỆU GIẢ LẬP ĐỂ TEST (bạn sẽ thay bằng logic thật) -----
        if (selectedDateStr != null) {
            // Giả lập có dữ liệu khi người dùng chọn một ngày
            availableSlots.add(createMockTimeSlot(LocalDateTime.of(dateToQuery, LocalTime.of(8,0))));
            availableSlots.add(createMockTimeSlot(LocalDateTime.of(dateToQuery, LocalTime.of(9,0))));
            availableSlots.add(createMockTimeSlot(LocalDateTime.of(dateToQuery, LocalTime.of(10,30))));
        }
        // -----------------------------------------------------------------

        model.addAttribute("availableSlots", availableSlots);
        model.addAttribute("selectedDate", dateToQuery.toString()); // Gửi lại ngày đã chọn (dạng YYYY-MM-DD)

        return "doctor/allhours"; // Đảm bảo tên file view đúng
    }

    // Hàm giả lập để test
    private TimeSlot createMockTimeSlot(LocalDateTime startTime) {
        TimeSlot slot = new TimeSlot();
        slot.setStartTime(startTime);
        slot.setEndTime(startTime.plusMinutes(30));
        slot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);
        return slot;
    }
    // XỬ LÝ VIỆC TẠO TIME SLOT HÀNG LOẠT
    @PostMapping("/doctor/working-hours/create")
    public String createWorkingHours(@RequestParam("workDate") String workDate,
                                     @RequestParam("startTime") String startTime,
                                     @RequestParam("endTime") String endTime,
                                     @RequestParam("slotDuration") int slotDuration,
                                     @AuthenticationPrincipal UserDetails userDetails,
                                     RedirectAttributes redirectAttributes) {
        try {
            doctorManageService.createTimeSlotsForDoctor(userDetails.getUsername(), workDate, startTime, endTime, slotDuration);
            redirectAttributes.addFlashAttribute("successMessage", "Đã tạo các khung giờ làm việc thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi tạo giờ làm việc: " + e.getMessage());
        }

        // Sau khi tạo xong, quay lại chính trang đó
        return "redirect:/doctor/working-hours";
    }

    @PostMapping("/doctor/appointment/cancel")
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId,
                                    @RequestParam("cancellationReason") String reason,
                                    RedirectAttributes redirectAttributes) {


        try {
            doctorManageService.cancelAppointment(appointmentId, reason);
            redirectAttributes.addFlashAttribute("successMessage", "Đã hủy lịch hẹn #" + appointmentId + " thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Lỗi khi hủy lịch hẹn: " + e.getMessage());
        }

        return "redirect:/doctor/appointment/" + appointmentId;
    }
    @GetMapping("/admin/chinh-sua-bac-si/{id}")
    public String hienThiFormChinhSua(@PathVariable Integer id, Model model) {
        University university = universityService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy trường với ID: " + id));
        model.addAttribute("university", university);
        return "admin/chinhsuatruongdaihoc";
    }
    @PostMapping("/admin/cap-nhat-bac-si")
    public String updateUniversity(@ModelAttribute UniversityDto universityDto, Model model) {
        UniversityDto response = universityService.updateUniversity(universityDto);

        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }

        // Trả về trang admin/update-university sau khi xử lý
        return "redirect:/admin/danh-sach-truong-dai-hoc";
    }


}
