package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "majorUni")
public class MajorUni {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "majoretails_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "majorDetails_majorUni_fk"))
    private MajorDetails majorDetails;
    @ManyToOne
    @JoinColumn(name = "university_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "university_majorUni_fk"))
    private University university;

}
