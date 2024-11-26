package com.example.admission.admissionswebsite.Controller;


import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class UniversityThymeleafController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversityService universityService;

    // Hiển thị form thêm trường đại học
//    @GetMapping("/them-truong-dai-hoc")
//    public String themtruongdaihoc(Model model) {
//        List<Users> usersList = userRepository.findAll();
//        model.addAttribute("usersList", usersList);
//        model.addAttribute("university", new University());
//        return "admin/themtruong";
//    }

    @GetMapping("/them-truong-dai-hoc")
    public String showAddUniversityForm(Model model) {
        model.addAttribute("university", new UniversityDto());
        return "admin/themtruong"; // Đây phải là đường dẫn chính xác đến template
    }

    @PostMapping("/them-truong-dai-hoc")
    public String addUniversity(@ModelAttribute UniversityDto universityDto) {
        universityService.addUniversity(universityDto);
        return "redirect:/admin/list"; // Chuyển hướng đến danh sách
    }


    // Hiển thị danh sách các trường đại học
    @GetMapping("/list")
    public String getAllUniversities(Model model) {
        model.addAttribute("universities", universityService.getAllUniversities());
        return "admin/danhsachtruongdaihoc"; // Thymeleaf sẽ render file templates/university/list.html
    }
}