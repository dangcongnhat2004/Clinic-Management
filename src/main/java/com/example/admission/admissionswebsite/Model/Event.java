package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nameEvent;
    private String description;
    private String moduleCourse;
    private String imageEvent;
    private String dateEvent;
    private String ownerName;
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "university_event_fk"))
    private University university;
}
