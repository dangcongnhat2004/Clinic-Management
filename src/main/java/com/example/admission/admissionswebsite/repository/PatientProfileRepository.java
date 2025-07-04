package com.example.admission.admissionswebsite.repository;


import com.example.admission.admissionswebsite.Model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientProfileRepository extends JpaRepository<PatientProfile, Long> {
    // Thêm phương thức này để tìm hồ sơ bệnh nhân bằng email của user
    Optional<PatientProfile> findByUser_Email(String email);

    List<PatientProfile> findAllByUser_Email(String email);
}