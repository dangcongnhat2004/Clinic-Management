package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@Data
@Table(name = "admission")
public class Admission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String content;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private Date postDate;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_addmission_fk"))
    private Users users;
}
