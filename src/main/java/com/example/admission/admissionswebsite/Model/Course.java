package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Data
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nameCourse;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String moduleCourse;
    @Lob
    private byte[] videoCourse;
    @ManyToOne
    @JoinColumn(name = "majorDetails_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "majorDetails_course_fk"))
    private MajorDetails majorDetails;
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "university_course_fk"))
    private University university;
}
