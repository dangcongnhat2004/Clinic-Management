package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)  // Đảm bảo không null
    private String majorGroupName;
    @Column(nullable = false)  // Đảm bảo không null
    private String majorImage;
    @Column(nullable = false)  // Đảm bảo không null
    private String description;
//    @ManyToOne
//    @JoinColumn(name = "university_id", referencedColumnName = "id", nullable = true, foreignKey = @ForeignKey(name = "university_major_fk"))
//    private University university;


}
