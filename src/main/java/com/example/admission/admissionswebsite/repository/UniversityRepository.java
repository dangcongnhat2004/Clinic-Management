package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface UniversityRepository extends JpaRepository<University,Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM University u WHERE u.id = :id")
    void deleteByIdCustom(Integer id);
    @Query("SELECT u FROM University u WHERE u.id = :id")
    Optional<University> findById(@Param("id") Long id);

    List<University> findTop4ByOrderByIdDesc();
}
