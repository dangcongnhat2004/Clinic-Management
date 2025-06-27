package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.SpecialtyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyDetailRepository extends JpaRepository<SpecialtyDetail,Integer> {
    List<SpecialtyDetail> findBySpecialtyId(int specialtyId);
}
