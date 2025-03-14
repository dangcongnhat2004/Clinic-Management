package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Data
@Table(name = "major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String majorGroupName;
    private String majorImage;
    @Column(columnDefinition = "TEXT")
    private String description;
    // Đảm bảo không null
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "university_major_fk"))
    private University university;

    // Thêm danh sách MajorDetails
    @OneToMany(mappedBy = "major", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MajorDetails> majorDetailsList;
}
