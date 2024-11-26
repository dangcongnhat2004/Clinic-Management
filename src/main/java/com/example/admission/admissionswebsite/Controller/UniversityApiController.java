package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.UniversityDto;

import com.example.admission.admissionswebsite.repository.UniversityRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class UniversityApiController {
    @Autowired
    private UniversityService universityService;

    // API để thêm trường đại học
    @PostMapping("/them-truong-dai-hoc")
    public ResponseEntity<UniversityDto> addUniversity(@RequestBody UniversityDto universityDto) {
        UniversityDto savedUniversity = universityService.addUniversity(universityDto);
        return ResponseEntity.ok(savedUniversity);
    }

    // API để lấy danh sách các trường đại học
    @GetMapping("/danh-sach-truong-dai-hoc")
    public ResponseEntity<List<UniversityDto>> getAllUniversities() {
        List<UniversityDto> universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }



}
