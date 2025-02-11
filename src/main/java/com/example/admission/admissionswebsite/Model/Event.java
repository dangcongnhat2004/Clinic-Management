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
    @Column(columnDefinition = "TEXT")
    private String description;
    private String imageEvent;
    private String dateEvent;
    private String ownerName;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_event_fk"))
    private Users users;
}
