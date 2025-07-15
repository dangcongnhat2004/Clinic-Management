package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Model.*;
import com.example.admission.admissionswebsite.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EnduserService {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private MajorRepository majorRepository;
    @Autowired
    private SpecialtyRepository specialtyRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorsRepository doctorsRepository;
    @Autowired
    private AdminPostRepository adminPostRepository;
    public List<University> getAllUniversities() {
        return universityRepository.findTop4ByOrderByIdDesc();
    }
//    public List<Major> getAllMajor() {
//        return majorRepository.findTop4ByOrderByIdDesc();
//    }
//    public List<Event> getAllEvent() {
//        return eventRepository.findTop3ByOrderByIdDesc();
//    }
    public List<Users> getAllDoctor() {
        return doctorRepository.findTop3ByRolesOrderByIdDesc("DOCTOR");
    }
    public List<Doctor> getAllDoctorsAsDoctorObjects() {
        return doctorsRepository.findAll();
    }
    public List<Doctor> findTop3ByOrderByIdDesc() {
        return doctorsRepository.findTop3ByOrderByIdDesc();
    }
    public List<Specialty> getAllSpecialty() {
        return specialtyRepository.findTop4ByOrderByIdDesc();
    }

    public List<AdminPost> getAllPost() {
        return adminPostRepository.findTop3ByOrderByIdDesc();
    }
    @Transactional(readOnly = true)
    public Page<Major> getAllMajors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return majorRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Page<University> getAllUniversity(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return universityRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Page<AdminPost> getAllAdmissionPost(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return adminPostRepository.findAll(pageable);
    }
}
