package com.example.admission.admissionswebsite.repository;


import com.example.admission.admissionswebsite.Model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface  OurUserRepo extends JpaRepository<Users, Integer> {
    Optional<Users> findByEmail(String email);
    // Lấy danh sách các đối tượng Map chứa id, name và numberPhone của mỗi người dùng
//    @Query("SELECT new map(u.id as id, u.nameUser as nameUser, u.numberPhone as numberPhone) FROM Users u")
//    List<Map<String, Object>> findAllIdNameAndPhoneNumbers();
//
//    @Query("SELECT new map(u.id as id, u.nameUser as name, u.numberPhone as numberPhone, u.schoolName as schoolName) FROM Users u WHERE u.email = :email")
//    Map<String, Object> findUserByEmail(@Param("email") String email);
//    default Map<String, Object> findHomepage() {
//        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        Long userId = Long.parseLong(userDetails.getUsername());
//
//        Map<String, Object> userInfo = findUserByEmail(String.valueOf(userId));
//
//        Users user = findById(Math.toIntExact(userId)).orElse(null);
//        if (user != null && user.getScholastics() != null && !user.getScholastics().isEmpty()) {
//            userInfo.put("nameYear", user.getScholastics().get(0).getNameYear());
//        } else {
//            userInfo.put("nameYear", null);
//        }
//
//        return userInfo;
//    }
//    @Query("SELECT u.numberPhone FROM Users u")
//    List<String> findAllPhoneNumbers();
    // Tìm tất cả các người dùng và lấy danh sách số điện thoại từ các người dùng đó
//    List<Users> findAll();
//
//    // Sau khi lấy tất cả các người dùng, bạn có thể lặp qua danh sách này và lấy số điện thoại từ mỗi người dùng
//    default List<String> findAllPhoneNumbers() {
//        List<Users> allUsers = findAll();
//        List<String> phoneNumbers = new ArrayList<>();
//
//        for (Users user : allUsers) {
//            phoneNumbers.add(user.getNumberPhone());
//        }
//
//        return phoneNumbers;
//    }

}
