package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Entity
@Getter
@Setter
@Data
@Table(name = "Specialty")
public class Specialty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String specialtyName;
    private String specialtyImage;
    @Column(columnDefinition = "TEXT")
    private String description;
    // Đảm bảo không null
    @ManyToOne
    @JoinColumn(name = "Users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "Users_specialty_fk"))
    private Users users;

    // Thêm danh sách MajorDetails
    @OneToMany(mappedBy = "specialty", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SpecialtyDetail> specialDetailsList;
}