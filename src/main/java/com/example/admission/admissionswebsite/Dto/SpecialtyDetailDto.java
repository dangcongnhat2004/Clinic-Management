package com.example.admission.admissionswebsite.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class SpecialtyDetailDto {
    private int id;
    private String nameSpecialty;
    private String SpecialtyCode;
    private String admissionBlock;
    private Integer specialtyId; // ID của nhóm ngành (Major)
    private String specialtyName; // Tên nhóm ngành
    private int statusCode;
    private String message;
    private List<SpecialtyDto> ourSpecialty;
}
