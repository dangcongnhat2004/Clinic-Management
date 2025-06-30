package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Dto.SpecialtyDto;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.Specialty;
import com.example.admission.admissionswebsite.service.MajorService;
import com.example.admission.admissionswebsite.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller

public class SpecialtyController {
    @Autowired
    private SpecialtyService specialtyService;

    @Value("${upload.specialty}")
    private String uploadPath;

    @GetMapping("/admin/them-chuyen-khoa")
    public String addSpecialtyForm() {
        return "specialty/themchuyenkhoa"; // Thymeleaf render trang thêm nhóm ngành
    }

    @PostMapping("/admin/them-chuyen-khoa")
    public String addSpecialty(@ModelAttribute SpecialtyDto specialtyDto,
                           @RequestParam("file") MultipartFile file,
                           Model model) {
        SpecialtyDto response = specialtyService.addSpecialty(specialtyDto, file);

        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }

        return "specialty/themchuyenkhoa"; // Reload trang với thông báo thành công hoặc lỗi
    }

    @GetMapping("/admin/danh-sach-chuyen-khoa")
    public String getListSpecialty(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            Page<Specialty> specialtyPage = specialtyService.getAllSpecialty(page, size);
            model.addAttribute("specialty", specialtyPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", specialtyPage.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách chuyên khoa: " + e.getMessage());
        }
        return "specialty/danhsachchuyenkhoa";
    }

    @GetMapping("/chi-tiet-chuyen-khoa/{id}")
    public String showSpecialtyDetail(@PathVariable Integer id, Model model) {
        Specialty specialty = specialtyService.getSpecialById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy chuyên khoa với ID: " + id));

        model.addAttribute("specialty", specialty);
        return "/user/specialtydetail"; // View hiển thị chi tiết chuyên khoa
    }

    @PostMapping("/admin/xoa-chuyen-khoa/{id}")
    public String deleteSpecialty(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            SpecialtyDto response = specialtyService.deleteSpecialty(id);
            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa chuyên khoa thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa chuyên khoa.");
        }
        return "redirect:/admin/danh-sach-chuyen-khoa";
    }

    @GetMapping("/admin/chinh-sua-chuyen-khoa/{id}")
    public String editSpecialtyForm(@PathVariable Integer id, Model model) {
        Specialty specialty = specialtyService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm ngành với ID: " + id));
        model.addAttribute("specialty", specialty);
        return "specialty/chinhsuachuyenkhoa"; // View chỉnh sửa nhóm ngành
    }

    @PostMapping("/admin/chinh-sua-chuyen-khoa")
    public String updateSpecialty(@ModelAttribute SpecialtyDto specialtyDto, Model model) {
        SpecialtyDto response = specialtyService.updateSpecialty(specialtyDto);
        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/admin/danh-sach-chuyen-khoa";
    }



}
