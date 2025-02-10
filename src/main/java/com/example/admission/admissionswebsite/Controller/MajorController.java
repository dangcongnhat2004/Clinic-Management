package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.service.MajorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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



//    @GetMapping("/danh-sach-nhom-nganh")
//    public String getListMajors(Model model,
//                                @RequestParam(defaultValue = "0") int page,
//                                @RequestParam(defaultValue = "10") int size) {
//        try {
//            if (page < 0) {
//                page = 0;
//            }
//            Page<Major> majorPage = majorService.getAllMajors(page, size);
//            model.addAttribute("majors", majorPage.getContent());
//            model.addAttribute("currentPage", page);
//            model.addAttribute("totalPages", majorPage.getTotalPages());
//        } catch (Exception e) {
//            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách nhóm ngành: " + e.getMessage());
//        }
//        return "majors/danhsachnhomnganh";
//    }
//
//    @PostMapping("/xoa-nhom-nganh/{id}")
//    public String deleteMajor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
//        try {
//            MajorDto response = majorService.deleteMajor(id);
//            if (response.getStatusCode() == 200) {
//                redirectAttributes.addFlashAttribute("successMessage", "Xóa nhóm ngành thành công!");
//            } else {
//                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
//            }
//        } catch (Exception e) {
//            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa nhóm ngành.");
//        }
//        return "redirect:/admin/danh-sach-nhom-nganh";
//    }
//
//    @GetMapping("/chinh-sua-nhom-nganh/{id}")
//    public String editMajorForm(@PathVariable Integer id, Model model) {
//        Major major = majorService.findById(id)
//                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm ngành với ID: " + id));
//        model.addAttribute("major", major);
//        return "majors/chinhsuanhomnganh"; // View chỉnh sửa nhóm ngành
//    }
//
//    @PostMapping("/cap-nhat-nhom-nganh")
//    public String updateMajor(@ModelAttribute MajorDto majorDto, Model model) {
//        MajorDto response = majorService.updateMajor(majorDto);
//        if (response.getStatusCode() == 200) {
//            model.addAttribute("successMessage", response.getMessage());
//        } else {
//            model.addAttribute("errorMessage", response.getMessage());
//        }
//        return "redirect:/admin/danh-sach-nhom-nganh";
//    }
}
