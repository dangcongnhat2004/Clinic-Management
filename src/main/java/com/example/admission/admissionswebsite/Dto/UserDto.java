package com.example.admission.admissionswebsite.Dto;

import com.example.admission.admissionswebsite.Model.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String nameUser;
    private String email;
    private String address;
    private String password;
    private String phoneNumber;
    private String birthDate;
    private String status;
    private String nameClass;
    private Users ourUsers;
    private String fullName;
    private String highSchoolName;
    private String gender;
    private String roles;  // Lưu vai trò dưới dạng chuỗi    // Getter và Setter cho roles

    //    private BigDecimal frequentScore1;
//    private BigDecimal frequentScore2;
//    private BigDecimal frequentScore3;
//    private BigDecimal frequentScore4;
//    private BigDecimal frequentScore5;
//    private BigDecimal midtermScore;
//    private BigDecimal finalScore;
//    private BigDecimal comments;
    private Users ourUser;
    // Constructor mặc định nếu cần
    public UserDto() {
    }



    // Constructor cho việc tạo đối tượng từ email và password
    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getter và Setter nếu cần
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
