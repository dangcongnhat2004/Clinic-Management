package com.example.admission.admissionswebsite.Dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Getter
@Setter
public class SpecialtyDto {
    private int id;
    private String specialtyName;
    private MultipartFile specialtyImage;
    private String description;
    private Integer universityId;
    private String universityName;
    private int statusCode;
    private String message;

    private List<SpecialtyDto> ourSpecial; // Chứa danh sách UserDto thay vì một User đơn

    public SpecialtyDto(int id, String specialtyName) {
        this.id = id;
        this.specialtyName = specialtyName;
    }


    public SpecialtyDto() {

    }
}
