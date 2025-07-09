package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DoctorsRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserAccount(Users userAccount);
    Optional<Doctor> findByUserAccount_Id(Long userId); // <-- Thêm phương thức này
    // THÊM PHƯƠNG THỨC MỚI NÀY
    @Query("SELECT d FROM Doctor d JOIN FETCH d.appointments WHERE d.userAccount.email = :email")
    Optional<Doctor> findByUserAccount_EmailWithAppointments(@Param("email") String email);
}
