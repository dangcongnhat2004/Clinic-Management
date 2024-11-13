package com.example.admission.admissionswebsite.Model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "userInfor")
public class UserInfor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String fullName;
    private String emailAddress;
    private String phoneNumber;
    private String majorFuture;
    private String highSchool;
    private String address;
    private String dreamSchool;
    //image
    private String sumTenImage;

    private String sumTen;
    //image

    private String sumElevenImage;

    private String sumEleven;
    //image

    private String sumTwelveImage;

    private String sumTwelve;
    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "users_userInfo_fk"))
    private Users users;

}
