package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpecialtyRepository extends JpaRepository<Specialty,Integer> {
    List<Specialty> findTop4ByOrderByIdDesc(); // Giả sử ID tăng dần theo thời gian, lấy 3 bản ghi mới nhất
    List<Specialty> findAll(); // Giả sử ID tăng dần theo thời gian, lấy 3 bản ghi mới nhất

}
