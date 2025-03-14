package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.MajorDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MajorDetailRepository extends JpaRepository<MajorDetails,Integer> {
    List<MajorDetails> findByMajorId(int majorId);
}
