package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MajorController {

    @Autowired
    private MajorService majorService;

    @Value("${upload.major}")
    private String uploadPath;

    @GetMapping("/admin/them-nhom-nganh")
    public String addMajorForm() {
        return "major/themnhomnganh"; // Thymeleaf render trang thêm nhóm ngành
    }

    @PostMapping("/admin/them-nhom-nganh")
    public String addMajor(@ModelAttribute MajorDto majorDto,
                           @RequestParam("file") MultipartFile file,
                           Model model) {
        MajorDto response = majorService.addMajor(majorDto, file);

        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }

        return "major/themnhomnganh"; // Reload trang với thông báo thành công hoặc lỗi
    }

    @GetMapping("/admin/danh-sach-nhom-nganh")
    public String getListMajors(Model model,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0) {
                page = 0;
            }
            Page<Major> majorPage = majorService.getAllMajors(page, size);
            model.addAttribute("majors", majorPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", majorPage.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách ngành học: " + e.getMessage());
        }
        return "major/danhsachnhomnganh";
    }

    @GetMapping("/chi-tiet-nganh-hoc/{id}")
    public String showMajordetail(@PathVariable Integer id, Model model) {
        Major major = majorService.getMajorById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm ngành với ID: " + id));

        model.addAttribute("major", major);
        return "/user/majordetail";
    }

    @PostMapping("/admin/xoa-nhom-nganh/{id}")
    public String deleteMajor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            MajorDto response = majorService.deleteMajorGroup(id);
            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa nhóm ngành thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa nhóm ngành.");
        }
        return "redirect:/admin/danh-sach-nhom-nganh";
    }

    @GetMapping("/admin/chinh-sua-nhom-nganh/{id}")
    public String editMajorForm(@PathVariable Integer id, Model model) {
        Major major = majorService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm ngành với ID: " + id));
        model.addAttribute("major", major);
        return "major/chinhsuanhomnganh"; // View chỉnh sửa nhóm ngành
    }

    @PostMapping("/admin/chinh-sua-nhom-nganh")
    public String updateMajor(@ModelAttribute MajorDto majorDto, Model model) {
        MajorDto response = majorService.updateMajorGroup(majorDto);
        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/admin/danh-sach-nhom-nganh";
    }
}
