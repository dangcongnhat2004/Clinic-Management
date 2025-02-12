package com.example.admission.admissionswebsite.repository;

import com.example.admission.admissionswebsite.Model.AdminPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminPostRepository extends JpaRepository<AdminPost,Integer> {
    List<AdminPost> findTop3ByOrderByIdDesc(); // Lấy 3 bài đăng mới nhất

}
