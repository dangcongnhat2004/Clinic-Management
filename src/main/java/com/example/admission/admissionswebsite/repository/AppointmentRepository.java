package com.example.admission.admissionswebsite.repository;


import com.example.admission.admissionswebsite.Model.Appointment;
import com.example.admission.admissionswebsite.Model.Doctor;
import com.example.admission.admissionswebsite.Model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientInOrderByTimeSlot_StartTimeDesc(List<PatientProfile> patients);
    List<Appointment> findByDoctor(Doctor doctor);
    List<Appointment> findByDoctorAndStatus(Doctor doctor, Appointment.AppointmentStatus status);
    @Query("SELECT DISTINCT a.patient FROM Appointment a WHERE a.doctor = :doctor AND a.status = :status")
    List<PatientProfile> findDistinctPatientsByDoctorAndStatus(@Param("doctor") Doctor doctor, @Param("status") Appointment.AppointmentStatus status);
}