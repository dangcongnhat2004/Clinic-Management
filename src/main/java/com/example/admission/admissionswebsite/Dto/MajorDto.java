package com.example.admission.admissionswebsite.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class MajorDto {
    private int id;
    private String majorGroupName;
    private MultipartFile majorImage;
    private String description;
    private Integer universityId;
    private String universityName;
    private int statusCode;
    private String message;
}
