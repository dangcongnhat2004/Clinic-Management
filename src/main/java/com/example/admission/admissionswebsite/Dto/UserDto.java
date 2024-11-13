package com.example.admission.admissionswebsite.Dto;

import com.example.admission.admissionswebsite.Model.Users;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReqRes {

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
    private String roles;
    private String nameClass;
    private Users ourUsers;
    private String fullName;
    private String highSchoolName;
    private String gender;


//    private BigDecimal frequentScore1;
//    private BigDecimal frequentScore2;
//    private BigDecimal frequentScore3;
//    private BigDecimal frequentScore4;
//    private BigDecimal frequentScore5;
//    private BigDecimal midtermScore;
//    private BigDecimal finalScore;
//    private BigDecimal comments;
    private Users ourUser;

}
