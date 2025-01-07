package com.example.admission.admissionswebsite.Dto;

import com.example.admission.admissionswebsite.Model.University;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UniversityDto {
    private int id; // ID của trường đại học
    private String nameSchool; // Tên trường
    private String address; // Địa chỉ
    private String description; // Mô tả về trường
    private String universityLogo;  // Sử dụng String thay vì byte[]
    private Integer userId; // ID của người dùng liên kết
    private int statusCode;
    private University ourUniversity;
    private String message;
    private String error;

    // Constructor từ `University` entity sang `UniversityDto`
    public UniversityDto(University university) {
        this.id = university.getId();
        this.nameSchool = university.getNameSchool();
        this.address = university.getAddress();
        this.description = university.getDescription();
        this.universityLogo = university.getUniversityLogo(); // Giữ nguyên byte[]
        this.userId = university.getUsers() != null ? Math.toIntExact(university.getUsers().getId()) : null;
    }

    // Constructor mặc định
    public UniversityDto() {}
}