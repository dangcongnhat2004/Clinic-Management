package com.example.admission.admissionswebsite.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
@Data
public class EventDto {
    private int id;
    private String nameEvent;
    private String description;
    private MultipartFile imageEvent;
    private String dateEvent;
    private String ownerName;
    private Integer userId;

    private int statusCode;
    private String message;
}