package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class UserController {
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
    @GetMapping("/")
    public String homePage() {
        return "/user/home";
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

    @GetMapping("/user/university")
    public String university() {
        return "user/university";
    }

    @GetMapping("/user/college")
    public String college() {
        return "user/college";
    }
    @GetMapping("/user/event")
    public String event() {
        return "user/event";
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
