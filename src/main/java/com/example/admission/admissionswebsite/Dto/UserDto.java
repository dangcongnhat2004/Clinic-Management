package com.example.admission.admissionswebsite.Dto;

import com.example.admission.admissionswebsite.Model.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

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
    private Integer id;
    //    private BigDecimal frequentScore1;
//    private BigDecimal frequentScore2;
//    private BigDecimal frequentScore3;
//    private BigDecimal frequentScore4;
//    private BigDecimal frequentScore5;
//    private BigDecimal midtermScore;
//    private BigDecimal finalScore;
//    private BigDecimal comments;
    private List<UserDto> ourUser; // Chứa danh sách UserDto thay vì một User đơn
    // Constructor mặc định nếu cần
    public UserDto() {
    }


    // Constructor khi truyền vào id và email


    // Constructor cho việc tạo đối tượng từ email và password
    public UserDto(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public UserDto(Integer id, String fullName, String email) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
    }

    public UserDto(Integer id,String email, String address, String phoneNumber, String birthDate, String fullName, String highSchoolName) {
        this.id = id;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.fullName = fullName;
        this.highSchoolName = highSchoolName;
    }

//    public UserDto(int intExact, String fullName, String email, String address, String birthDate, String highSchoolName, String phoneNumber) {
//    }
    //    public UserDto(String id, String email, String fullName) {
//    }
//    public UserDto(Long id) {
//    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getHighSchoolName() {
        return highSchoolName;
    }

    public void setHighSchoolName(String highSchoolName) {
        this.highSchoolName = highSchoolName;
    }
}
