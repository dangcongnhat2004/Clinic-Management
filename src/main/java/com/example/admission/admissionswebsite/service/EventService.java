package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.EventDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.Model.Users;
import com.example.admission.admissionswebsite.repository.EventRepository;
import com.example.admission.admissionswebsite.repository.UniversityRepository;
import com.example.admission.admissionswebsite.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Value("${upload.event}")
    private String uploadPath;
    @Autowired
    private UserRepository userRepository;
    public EventDto addEvent(EventDto eventDto, MultipartFile file) {
        EventDto response = new EventDto();
        try {
            if (eventDto == null) {
                response.setStatusCode(400);
                response.setMessage("Event data is required");
                return response;
            }
            if (eventDto.getNameEvent() == null || eventDto.getNameEvent().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên sự kiện");
                return response;
            }
            if (eventDto.getDescription() == null || eventDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả sự kiện");
                return response;
            }
            if (eventDto.getDateEvent() == null || eventDto.getDateEvent().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập ngày diễn ra sự kiện");
                return response;
            }
            if (file == null || file.isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng chọn hình ảnh sự kiện");
                return response;
            }
            if (eventDto.getUserId() == null) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng chọn người dùng");
                return response;
            }
            Users user = userRepository.findById(eventDto.getUserId()).orElse(null);
            if (user == null) {
                response.setStatusCode(404);
                response.setMessage("User không tồn tại");
                return response;
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path uploadDirPath = Paths.get(uploadPath);
            if (!Files.exists(uploadDirPath)) {
                Files.createDirectories(uploadDirPath);
            }
            Path filePath = uploadDirPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);

            Event event = new Event();

            event.setNameEvent(eventDto.getNameEvent());
            event.setDescription(eventDto.getDescription());
            event.setDateEvent(eventDto.getDateEvent());
            event.setImageEvent(fileName);
            event.setOwnerName(eventDto.getOwnerName());
            event.setUsers(user);

            Event savedEvent = eventRepository.save(event);

            response.setStatusCode(200);
            response.setMessage("Thêm sự kiện thành công");
            response.setNameEvent(savedEvent.getNameEvent());
            response.setDescription(savedEvent.getDescription());
            response.setDateEvent(savedEvent.getDateEvent());
            response.setOwnerName(savedEvent.getOwnerName());
        } catch (IOException e) {
            response.setStatusCode(500);
            response.setMessage("Error during file upload: " + e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi thêm sự kiện: " + e.getMessage());
        }
        return response;
    }

    @Transactional(readOnly = true)
    public Page<Event> getAllEvents(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable);
    }
    @Transactional(readOnly = true)
    public Page<Event> getAllEventlist(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return eventRepository.findAll(pageable);
    }

    public EventDto deleteEvent(Integer id) {
        EventDto response = new EventDto();
        try {
            Event event = eventRepository.findById(id).orElse(null);
            if (event == null) {
                response.setStatusCode(404);
                response.setMessage("Sự kiện không tồn tại.");
                return response;
            }
            String imagePath = uploadPath + "/" + event.getImageEvent();
            File imageFile = new File(imagePath);
            if (imageFile.exists() && !imageFile.delete()) {
                response.setStatusCode(500);
                response.setMessage("Không thể xóa ảnh của sự kiện.");
                return response;
            }
            eventRepository.delete(event);
            response.setStatusCode(200);
            response.setMessage("Xóa sự kiện thành công.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi xóa sự kiện: " + e.getMessage());
        }
        return response;
    }

    public Optional<Event> findById(Integer id) {
        return eventRepository.findById(id);
    }

    public EventDto updateEvent(EventDto eventDto) {
        EventDto response = new EventDto();
        try {
            // Validate dữ liệu
            if (eventDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu sự kiện không được để trống");
                return response;
            }
            if (eventDto.getNameEvent() == null || eventDto.getNameEvent().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tiêu đề sự kiện");
                return response;
            }
            if (eventDto.getDescription() == null || eventDto.getDescription().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mô tả sự kiện");
                return response;
            }

            // Tìm sự kiện theo ID
            Optional<Event> optionalEvent = eventRepository.findById(eventDto.getId());
            if (!optionalEvent.isPresent()) {
                response.setStatusCode(404);
                response.setMessage("Sự kiện không tồn tại");
                return response;
            }

            Event event = optionalEvent.get();

            // Cập nhật các trường cần thiết (ngoại trừ ảnh và ngày)
            event.setNameEvent(eventDto.getNameEvent());
            event.setDescription(eventDto.getDescription());
            event.setOwnerName(eventDto.getOwnerName());

            // Lưu sự kiện đã cập nhật
            Event updatedEvent = eventRepository.save(event);

            // Trả về response
            response.setStatusCode(200);
            response.setMessage("Cập nhật sự kiện thành công");
            response.setId(updatedEvent.getId());
            response.setNameEvent(updatedEvent.getNameEvent());
            response.setDescription(updatedEvent.getDescription());
            response.setOwnerName(updatedEvent.getOwnerName());

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi cập nhật sự kiện: " + e.getMessage());
        }
        return response;
    }

    public UserDto getUserIdsByUniversityRole() {
        UserDto resp = new UserDto();
        try {
            List<Users> users = userRepository.findByRoles("UNIVERSITY");
            if (users.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No users found with role 'university'");
                return resp;
            }
            List<UserDto> userDtos = users.stream()
                    .map(user -> new UserDto(
                            Math.toIntExact(user.getId()),
                            user.getEmail(),
                            user.getFullName()
                    ))
                    .collect(Collectors.toList());
            resp.setStatusCode(200);
            resp.setMessage("User IDs retrieved successfully");
            resp.setOurUser(userDtos); // Đảm bảo trả về danh sách UserDto
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError("Error while retrieving user IDs: " + e.getMessage());
        }
        return resp;
    }
}
