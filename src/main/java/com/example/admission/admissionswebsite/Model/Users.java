package com.example.admission.admissionswebsite.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Data
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String highSchoolName;
    private String username;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    private String birthDate;
    private String address;
    private String status;
    @Enumerated(EnumType.STRING)
    @Column(name = "roles")
    private Role role;  // Change from String to Role
    public enum Role {
        ADMIN,
        STUDENT,
        UNIVERSITY
    }

}
