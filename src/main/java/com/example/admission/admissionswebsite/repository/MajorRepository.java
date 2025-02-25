package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.Major;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface MajorRepository extends JpaRepository<Major,Integer> {
    List<Major> findTop4ByOrderByIdDesc(); // Giả sử ID tăng dần theo thời gian, lấy 3 bản ghi mới nhất
    List<Major> findAll(); // Giả sử ID tăng dần theo thời gian, lấy 3 bản ghi mới nhất

}
