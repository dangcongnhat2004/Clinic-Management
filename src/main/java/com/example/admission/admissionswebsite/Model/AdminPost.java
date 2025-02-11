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
@Table(name = "adminpost")
public class AdminPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String content;
    private Date postDate;
    //tên nguoi quan li bai dang
    private String ownerName;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_adminpost_fk"))
    private Users users;
}
