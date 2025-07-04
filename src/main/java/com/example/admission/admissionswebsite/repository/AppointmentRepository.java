package com.example.admission.admissionswebsite.repository;


import com.example.admission.admissionswebsite.Model.Appointment;
import com.example.admission.admissionswebsite.Model.PatientProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByPatientInOrderByTimeSlot_StartTimeDesc(List<PatientProfile> patients);

}