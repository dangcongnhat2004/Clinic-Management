package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event,Integer> {
    List<Event> findTop3ByOrderByIdDesc(); // Lấy 3 sự kiện mới nhất theo ID giảm dần

}
