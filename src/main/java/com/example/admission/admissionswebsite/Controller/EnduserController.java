package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.Specialty;
import com.example.admission.admissionswebsite.repository.AdminPostRepository;
import com.example.admission.admissionswebsite.repository.DoctorRepository;
import com.example.admission.admissionswebsite.repository.DoctorsRepository;
import com.example.admission.admissionswebsite.repository.SpecialtyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
public class EnduserController {

    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private DoctorsRepository doctorsRepository;
    @Autowired
    private AdminPostRepository postRepository;


    @GetMapping("/chi-tiet/chi-tiet-chuyen-khoa/{id}")
    public String viewSpecialtyDetails(@PathVariable("id") Integer specialtyId, Model model) {

        Specialty specialty = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chuyên khoa với ID: " + specialtyId));


        List<Doctor> doctors = doctorsRepository.findBySpecialty(specialty);

        model.addAttribute("specialty", specialty);
        model.addAttribute("doctors", doctors);

        return "user/specialty-details";
    }

    @GetMapping("/chi-tiet/danh-sach-chuyen-khoa")
    public String viewAllSpecialties(@RequestParam(name = "page", defaultValue = "0") int page,
                                     @RequestParam(name = "size", defaultValue = "3") int size,
                                     Model model) {


        Pageable pageable = PageRequest.of(page, size);

        Page<Specialty> specialtyPage = specialtyRepository.findAll(pageable);

        model.addAttribute("specialtyPage", specialtyPage);

        return "user/specialty-list";
    }

    @GetMapping("/chi-tiet/tin-tuc")
    public String viewAllPosts(@RequestParam(name = "page", defaultValue = "0") int page,
                               @RequestParam(name = "size", defaultValue = "6") int size, // Hiển thị 9 bài mỗi trang
                               Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("postDate").descending());
        Page<AdminPost> postPage = postRepository.findAll(pageable);
        model.addAttribute("postPage", postPage);
        return "user/post-list";
    }

    // CHI TIẾT TIN TỨC
    @GetMapping("/chi-tiet/tin-tuc/{id}")
    public String viewPostDetails(@PathVariable("id") Integer postId, Model model) {
        AdminPost post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bài viết với ID: " + postId));
        model.addAttribute("post", post);
        return "user/post-details";
    }
    @GetMapping("/chi-tiet/bac-si")
    public String viewAllDoctors(@RequestParam(name = "page", defaultValue = "0") int page,
                                 @RequestParam(name = "size", defaultValue = "6") int size,
                                 Model model) {
        // Lấy các bác sĩ có status là ACTIVE
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Doctor> doctorPage = doctorsRepository.findByStatus("ACTIVE", pageable);
        model.addAttribute("doctorPage", doctorPage);
        return "user/doctor-list";
    }

    // CHI TIẾT BÁC SĨ
    @GetMapping("/chi-tiet/bac-si/{id}")
    public String viewDoctorDetails(@PathVariable("id") Long doctorId, Model model) {
        // Dùng JOIN FETCH để lấy cả Specialty và UserAccount
        Doctor doctor = doctorsRepository.findWithDetailsById(doctorId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bác sĩ với ID: " + doctorId));
        model.addAttribute("doctor", doctor);
        return "user/doctor-details";
    }
}
