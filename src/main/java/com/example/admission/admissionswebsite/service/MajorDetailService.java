package com.example.admission.admissionswebsite.service;

import com.example.admission.admissionswebsite.Dto.MajorDetailDto;
import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.MajorDetails;
import com.example.admission.admissionswebsite.repository.MajorDetailRepository;
import com.example.admission.admissionswebsite.repository.MajorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MajorDetailService {

       @Autowired
       private MajorDetailRepository majorDetailRepository;



    public List<MajorDetails> getMajorDetailsByMajorId(int majorId) {
        return majorDetailRepository.findByMajorId(majorId);
    }
       @Autowired
       private MajorRepository majorRepository;

    public MajorDto getMajorGroups() {
        MajorDto resp = new MajorDto();
        try {
            List<Major> majorGroups = majorRepository.findAll();
            if (majorGroups.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("No major groups found");
                return resp;
            }

            List<MajorDto> majorDtos = majorGroups.stream()
                    .map(majorGroup -> new MajorDto(
                            majorGroup.getId(),
                            majorGroup.getMajorGroupName()
                    ))
                    .collect(Collectors.toList());

            resp.setStatusCode(200);
            resp.setMessage("Major groups retrieved successfully");
            resp.setOurMajor(majorDtos); // Đảm bảo trả về danh sách MajorDto

        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setMessage("Error while retrieving major groups: " + e.getMessage());
        }
        return resp;
    }
    public MajorDetailDto addMajor(MajorDetailDto majorDetailDto) {
        MajorDetailDto response = new MajorDetailDto();
        try {
            if (majorDetailDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu ngành là bắt buộc");
                return response;
            }
            if (majorDetailDto.getNameMajor() == null || majorDetailDto.getNameMajor().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên ngành");
                return response;
            }
            if (majorDetailDto.getMajorCode() == null || majorDetailDto.getMajorCode().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mã ngành");
                return response;
            }
            if (majorDetailDto.getAdmissionBlock() == null || majorDetailDto.getAdmissionBlock().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập khối xét tuyển");
                return response;
            }
            if (majorDetailDto.getMajorId() == null) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng chọn nhóm ngành");
                return response;
            }
            Major majorGroup = majorRepository.findById(majorDetailDto.getMajorId()).orElse(null);
            if (majorGroup == null) {
                response.setStatusCode(404);
                response.setMessage("Nhóm ngành không tồn tại");
                return response;
            }
            MajorDetails major = new MajorDetails();
            major.setNameMajor(majorDetailDto.getNameMajor());
            major.setMajorCode(majorDetailDto.getMajorCode());
            major.setAdmissionBlock(majorDetailDto.getAdmissionBlock());
            major.setMajor(majorGroup);
            MajorDetails savedMajor = majorDetailRepository.save(major);
            response.setStatusCode(200);
            response.setMessage("Thêm ngành thành công");
            response.setNameMajor(savedMajor.getNameMajor());
            response.setMajorCode(savedMajor.getMajorCode());
            response.setAdmissionBlock(savedMajor.getAdmissionBlock());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi thêm ngành: " + e.getMessage());
        }
        return response;
    }

    @Transactional(readOnly = true)
    public Page<MajorDetails> getAllMajorDetails(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return majorDetailRepository.findAll(pageable);
    }

    public MajorDetailDto deleteMajorGroup(Integer id) {
        MajorDetailDto response = new MajorDetailDto();
        try {
            // Tìm nhóm ngành theo ID
            MajorDetails majorDetails = majorDetailRepository.findById(id).orElse(null);
            if (majorDetails == null) {
                response.setStatusCode(404);
                response.setMessage("Nhóm ngành không tồn tại.");
                return response;
            }

            // Xóa bản ghi nhóm ngành
            majorDetailRepository.delete(majorDetails);
            response.setStatusCode(200);
            response.setMessage("Xóa nhóm ngành thành công.");
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi xóa nhóm ngành: " + e.getMessage());
        }
        return response;
    }
    public Optional<MajorDetails> findById(Integer id) {
        return majorDetailRepository.findById(id);
    }

    public MajorDetailDto updateMajorDetails(MajorDetailDto majorDetailDto) {
        MajorDetailDto response = new MajorDetailDto();
        try {
            // Validate dữ liệu đầu vào
            if (majorDetailDto == null) {
                response.setStatusCode(400);
                response.setMessage("Dữ liệu ngành không được để trống");
                return response;
            }
            if (majorDetailDto.getNameMajor() == null || majorDetailDto.getNameMajor().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập tên ngành");
                return response;
            }
            if (majorDetailDto.getMajorCode() == null || majorDetailDto.getMajorCode().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập mã ngành");
                return response;
            }
            if (majorDetailDto.getAdmissionBlock() == null || majorDetailDto.getAdmissionBlock().isEmpty()) {
                response.setStatusCode(400);
                response.setMessage("Vui lòng nhập khối xét tuyển");
                return response;
            }

            // Tìm ngành theo ID
            Optional<MajorDetails> optionalMajor = majorDetailRepository.findById(majorDetailDto.getId());
            if (!optionalMajor.isPresent()) {
                response.setStatusCode(404);
                response.setMessage("Ngành không tồn tại");
                return response;
            }

            MajorDetails majorDetails = optionalMajor.get();

            // Cập nhật các trường bằng dữ liệu từ DTO
            majorDetails.setNameMajor(majorDetailDto.getNameMajor());
            majorDetails.setMajorCode(majorDetailDto.getMajorCode());
            majorDetails.setAdmissionBlock(majorDetailDto.getAdmissionBlock());

            // Lưu ngành đã cập nhật
            MajorDetails updatedMajor = majorDetailRepository.save(majorDetails);

            // Trả về response
            response.setStatusCode(200);
            response.setMessage("Cập nhật ngành thành công");
            response.setId(updatedMajor.getId());
            response.setNameMajor(updatedMajor.getNameMajor());
            response.setMajorCode(updatedMajor.getMajorCode());
            response.setAdmissionBlock(updatedMajor.getAdmissionBlock());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Đã xảy ra lỗi khi cập nhật ngành: " + e.getMessage());
        }
        return response;
    }


}
