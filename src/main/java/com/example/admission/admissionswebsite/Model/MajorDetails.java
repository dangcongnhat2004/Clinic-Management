package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "majordetails")
public class MajorDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nameMajor;
    private String majorCode;
    private String admissionBlock;

    @ManyToOne
    @JoinColumn(name = "major_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "major_majordetails_fk"))
    private Major major;

}
