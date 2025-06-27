package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "specialtyDetail")
public class SpecialtyDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nameMajor;
    private String majorCode;
    private String admissionBlock;

    @ManyToOne
    @JoinColumn(name = "specialty_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "specialty_specialtyDetail_fk"))
    private Specialty specialty;

}
