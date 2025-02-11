package com.example.admission.admissionswebsite.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class MajorDto {
    private int id;
    private String majorGroupName;
    private MultipartFile majorImage;
    private String description;
    private Integer universityId;
    private String universityName;
    private int statusCode;
    private String message;

    private List<MajorDto> ourMajor; // Chứa danh sách UserDto thay vì một User đơn

    public MajorDto(int id, String majorGroupName) {
        this.id = id;
        this.majorGroupName = majorGroupName;
    }


    public MajorDto() {

    }
}
