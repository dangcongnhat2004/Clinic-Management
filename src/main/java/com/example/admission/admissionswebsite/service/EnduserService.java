package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.repository.AdminPostRepository;
import com.example.admission.admissionswebsite.repository.EventRepository;
import com.example.admission.admissionswebsite.repository.MajorRepository;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
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
    private EventRepository eventRepository;
    @Autowired
    private AdminPostRepository adminPostRepository;
    public List<University> getAllUniversities() {
        return universityRepository.findTop4ByOrderByIdDesc();
    }
    public List<Major> getAllMajor() {
        return majorRepository.findTop4ByOrderByIdDesc();
    }
    public List<Event> getAllEvent() {
        return eventRepository.findTop3ByOrderByIdDesc();
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
