package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Integer> {
}
