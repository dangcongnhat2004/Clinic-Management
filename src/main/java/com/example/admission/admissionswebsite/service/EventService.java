package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.repository.AdminPostRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EventService {
    @Autowired
    private AdminPostRepository adminPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.paths}")
    private String uploadPath;
}
