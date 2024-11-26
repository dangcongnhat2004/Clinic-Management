package com.example.admission.admissionswebsite.repository;


import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
    Optional<Users> findByEmail(String email);

}
