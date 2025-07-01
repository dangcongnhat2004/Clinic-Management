package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private AdminPostService adminPostService;
    @Autowired
    private EnduserService enduserService;
    @Autowired
    private EventService eventService;
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
        List<Event> events = enduserService.getAllEvent();
        model.addAttribute("events", events);
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
    @GetMapping("/dat-lich")
    public String appointments(Model model) {

//        model.addAttribute("uploadPath", uploadPath); // Thêm uploadPath vào model
        return "/user/appointments"; // Thymeleaf sẽ render file templates/admin/danhsachtruongdaihoc.html
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

    @GetMapping("/user/college")
    public String college() {
        return "user/college";
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
