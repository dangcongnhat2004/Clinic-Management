package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    List<Users> findByRoles(String role);
    @Query("SELECT u FROM University u WHERE u.id = :id")
    Optional<Users> findById(@Param("id") Long id);
}
