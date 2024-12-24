package com.example.admission.admissionswebsite.service;



import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.OurUserRepo;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class AuthService {

    @Autowired
    private OurUserRepo ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public UserDto signUp(UserDto registrationRequest){
        UserDto resp = new UserDto();
        if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
            resp.setStatusCode(400);
            resp.setMessage("Email already registered");
            return resp;
        }

        try {
            Users ourUsers = new Users();
            ourUsers.setEmail(registrationRequest.getEmail());
            ourUsers.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            ourUsers.setRoles("STUDENT");
            ourUsers.setAddress(registrationRequest.getAddress());
            ourUsers.setFullName(registrationRequest.getFullName());
            ourUsers.setBirthDate(registrationRequest.getBirthDate());
            ourUsers.setGender(registrationRequest.getGender());
            ourUsers.setPhoneNumber(registrationRequest.getPhoneNumber());
            ourUsers.setHighSchoolName(registrationRequest.getHighSchoolName());
            Users ourUserResult = ourUserRepo.save(ourUsers);

            if (ourUserResult != null && ourUserResult.getId()>0) {
                resp.setOurUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        }catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Error during user registration: " + e.getMessage());
        }

        return resp;
    }

    public UserDto signIn(UserDto signinRequest) {
        UserDto response = new UserDto();
        try {
            // Xác thực
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signinRequest.getEmail(), signinRequest.getPassword()));

            // Lấy thông tin người dùng từ database
            var user = ourUserRepo.findByEmail(signinRequest.getEmail()).orElseThrow();
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            // Thiết lập giá trị cho response
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setMessage("Successfully Signed In");
            response.setRoles(user.getRoles());  // Đảm bảo `roles` không null

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }



//    public UserDto refreshToken(UserDto refreshTokenReqiest){
//        UserDto response = new UserDto();
//        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
//        Users users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
//        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(), (UserDetails) users)) {
//            var jwt = jwtUtils.generateToken((UserDetails) users);
//            response.setStatusCode(200);
//            response.setToken(jwt);
//            response.setRefreshToken(refreshTokenReqiest.getToken());
//            response.setExpirationTime("24Hr");
//            response.setMessage("Successfully Refreshed Token");
//        }
//        response.setStatusCode(500);
//        return response;
//    }
public UserDto refreshToken(UserDto refreshTokenRequest) {
    UserDto response = new UserDto();
    try {
        String email = jwtUtils.extractUsername(refreshTokenRequest.getToken());
        Users user = ourUserRepo.findByEmail(email).orElseThrow();

        if (jwtUtils.isTokenValid(refreshTokenRequest.getToken(), user)) {
            String newJwt = jwtUtils.generateToken(user);
            response.setStatusCode(200);
            response.setToken(newJwt);
            response.setRefreshToken(refreshTokenRequest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Token refreshed successfully");
        }
    } catch (ExpiredJwtException e) {
        response.setStatusCode(401);
        response.setError("Token expired. Please log in again.");
    }
    return response;
}

}
