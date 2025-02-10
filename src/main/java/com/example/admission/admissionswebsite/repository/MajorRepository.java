package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface MajorRepository extends JpaRepository<Major,Integer> {
}
