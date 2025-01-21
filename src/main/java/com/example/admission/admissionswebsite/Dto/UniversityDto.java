package com.example.admission.admissionswebsite.Dto;

import com.example.admission.admissionswebsite.Model.University;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UniversityDto {
    private int id; // ID của trường đại học
    private String nameSchool; // Tên trường
    private String address; // Địa chỉ
    private String description; // Mô tả về trường
    private MultipartFile universityLogo;  // Sử dụng String thay vì byte[]
    private Integer userId; // ID của người dùng liên kết
    private int statusCode;
    private University ourUniversity;
    private String message;
    private String error;
    private String uniCode;
    private String universityLogoPath;   // Dùng cho lưu trữ đường dẫn file


    // Constructor từ `University` entity sang `UniversityDto`


    public UniversityDto(int id, String nameSchool, String address, String description, MultipartFile universityLogo, Integer userId, int statusCode, University ourUniversity, String message, String error, String uniCode, String universityLogoPath) {
        this.id = id;
        this.nameSchool = nameSchool;
        this.address = address;
        this.description = description;
        this.universityLogo = universityLogo;
        this.userId = userId;
        this.statusCode = statusCode;
        this.ourUniversity = ourUniversity;
        this.message = message;
        this.error = error;
        this.uniCode = uniCode;
        this.universityLogoPath = universityLogoPath;
    }

    // Constructor mặc định
    public UniversityDto() {}
}