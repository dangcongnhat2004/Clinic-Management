package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorsRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserAccount(Users userAccount);
    Optional<Doctor> findByUserAccount_Id(Long userId); // <-- Thêm phương thức này

}
