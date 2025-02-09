package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UserDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MajorController {
    @GetMapping("/admin/them-nganh-hoc")
    public String addMajor(Model model) {

            return "404";


    }
}
