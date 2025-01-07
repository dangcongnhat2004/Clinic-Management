package com.example.admission.admissionswebsite.Controller;


import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller

public class UniveristyController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UniversityService universityService;
    @Autowired
    private AdminService adminService;
    // Hiển thị form thêm trường đại học
//    @GetMapping("/them-truong-dai-hoc")
//    public String themtruongdaihoc(Model model) {
//        List<Users> usersList = userRepository.findAll();
//        model.addAttribute("usersList", usersList);
//        model.addAttribute("university", new University());
//        return "admin/themtruong";
//    }
    @GetMapping("/admin/them-truong-dai-hoc")
    public String getUserIds(Model model) {

        // Gọi phương thức getUserIdsByUniversityRole từ Service
        UserDto response = adminService.getUserIdsByUniversityRole();
        if (response.getStatusCode() == 200) {
            // Nếu mã trạng thái là 200, thêm danh sách userDtos vào model
            model.addAttribute("usersList", response.getOurUser()); // usersList là List<UserDto>
            return "admin/themtruong";  // Trả về view danh sách người dùng
        } else {
            // Nếu có lỗi, thêm thông báo lỗi vào model
            model.addAttribute("errorMessage", response.getMessage());
            return "404";  // Trả về trang lỗi (hoặc trang chính có thông báo lỗi)
        }
    }


    @GetMapping("/test")
    public String test() {
        System.out.println("API Test called");
        return "Test OK";
    }


    @PostMapping("/admin/them-truong-dai-hoc")
    public String addUniversity(@ModelAttribute UniversityDto universityDto,
                                @RequestParam("universityLogo") MultipartFile file,
                                @RequestParam("usersId") Integer userId,
                                Model model) {
        System.out.println("API addUniversity called");

        try {
            // Set the user ID in the DTO
            universityDto.setUserId(userId);

            // Call the service method with `universityDto` and `file`
            UniversityDto response = universityService.addUniversity(universityDto, file);

            if (response.getStatusCode() == 200) {
                // If adding the university is successful, redirect to the list page
                return "redirect:/admin/danh-sach-truong-dai-hoc";
            } else {
                // If there's an error, display the error message on the interface
                model.addAttribute("errorMessage", response.getMessage());
                return "admin/them-truong-dai-hoc";
            }
        } catch (Exception e) {
            // Handle exceptions if there's an error during the process
            model.addAttribute("errorMessage", "An error occurred while adding the university. Please try again.");
            return "admin/404";
        }
    }



    // Hiển thị danh sách các trường đại học
    @GetMapping("/admin/danh-sach-truong-dai-hoc")
    public String getAllUniversities(Model model) {
        return "admin/danhsachtruongdaihoc"; // Thymeleaf sẽ render file templates/university/list.html
    }
}