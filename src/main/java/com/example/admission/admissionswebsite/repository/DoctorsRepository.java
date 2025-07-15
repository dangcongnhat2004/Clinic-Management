package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.Specialty;
import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DoctorsRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByUserAccount(Users userAccount);
    Optional<Doctor> findByUserAccount_Id(Long userId); // <-- Thêm phương thức này
    // THÊM PHƯƠNG THỨC MỚI NÀY
    @Query("SELECT d FROM Doctor d JOIN FETCH d.appointments WHERE d.userAccount.email = :email")
    Optional<Doctor> findByUserAccount_EmailWithAppointments(@Param("email") String email);
    List<Doctor> findTop3ByOrderByIdDesc(); // Giả sử ID tăng dần theo thời gian, lấy 3 bản ghi mới nhất
    List<Doctor> findBySpecialty(Specialty specialty);
    Page<Doctor> findByStatus(String status, Pageable pageable);
    List<Doctor> findBySpecialty_Id(Long id);
    List<Doctor> findByFullNameContainingIgnoreCase(String fullName);

    // Tìm bác sĩ theo ID và tải kèm các thông tin liên quan
    @Query("SELECT d FROM Doctor d LEFT JOIN FETCH d.specialty LEFT JOIN FETCH d.userAccount WHERE d.id = :id")
    Optional<Doctor> findWithDetailsById(@Param("id") Long id);
}
