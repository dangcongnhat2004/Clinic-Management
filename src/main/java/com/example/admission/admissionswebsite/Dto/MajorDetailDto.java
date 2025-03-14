package com.example.admission.admissionswebsite.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
public class MajorDetailDto {
    private int id;
    private String nameMajor;
    private String majorCode;
    private String admissionBlock;
    private Integer majorId; // ID của nhóm ngành (Major)
    private String majorGroupName; // Tên nhóm ngành
    private int statusCode;
    private String message;
    private List<MajorDto> ourMajor;
}
