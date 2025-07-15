package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository extends JpaRepository<MedicalRecord, Long> {
    List<MedicalRecord> findByAppointment_DoctorOrderByIdDesc(Doctor doctor);
    @Query("SELECT mr FROM MedicalRecord mr " +
            "LEFT JOIN FETCH mr.appointment a " +
            "LEFT JOIN FETCH a.patient p " +
            "LEFT JOIN FETCH a.doctor d " +
            "LEFT JOIN FETCH a.timeSlot ts " +
            "WHERE mr.id = :id")
    Optional<MedicalRecord> findByIdWithDetails(@Param("id") Long id);
}
