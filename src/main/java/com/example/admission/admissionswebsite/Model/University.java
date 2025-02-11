package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "university")
public class University {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)  // Ensure it's unique and non-null

    private int id;

    private String nameSchool;
    private String address;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String universityLogo;
    private String uniCode;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_university_fk"))
    private Users users;
}
