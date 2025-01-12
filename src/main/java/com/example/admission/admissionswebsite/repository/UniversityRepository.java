package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface UniversityRepository extends JpaRepository<University,Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM University u WHERE u.id = :id")
    void deleteByIdCustom(Integer id);
}
