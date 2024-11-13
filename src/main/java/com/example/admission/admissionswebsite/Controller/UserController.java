package com.example.admission.admissionswebsite.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @GetMapping("/user")
    public String homeuser() {
        return "user/index";
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
