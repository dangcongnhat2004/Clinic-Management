package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.UniversityDto;
import com.example.admission.admissionswebsite.Model.University;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UniversityService {
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private UserRepository userRepository;

    public UniversityDto addUniversity(UniversityDto universityDto) {
        University university = new University();
        university.setNameSchool(universityDto.getNameSchool());
        university.setAddress(universityDto.getAddress());
        university.setDescription(universityDto.getDescription());
        university.setUniversityLogo(universityDto.getUniversityLogo());

        // Tìm `Users` bằng `userId` từ `UniversityDto`
        if (universityDto.getUserId() != null) {
            Optional<Users> userOptional = userRepository.findById(universityDto.getUserId());
            userOptional.ifPresent(university::setUsers);  // Nếu tìm thấy user, thiết lập cho university
        }

        University savedUniversity = universityRepository.save(university);
        return new UniversityDto(savedUniversity); // Chuyển đổi Entity thành DTO
    }

    public List<UniversityDto> getAllUniversities() {
        return universityRepository.findAll().stream()
                .map(UniversityDto::new)
                .collect(Collectors.toList());
    }
}
