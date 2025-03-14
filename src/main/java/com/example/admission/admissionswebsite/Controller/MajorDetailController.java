package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Dto.MajorDetailDto;
import com.example.admission.admissionswebsite.Dto.MajorDto;
import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.Model.Major;
import com.example.admission.admissionswebsite.Model.MajorDetails;
import com.example.admission.admissionswebsite.service.MajorDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MajorDetailController {
    @Autowired
    private MajorDetailService majorDetailService;
    @GetMapping("/admin/them-nganh")
    public String showAddMajorForm(Model model) {
        MajorDto response = majorDetailService.getMajorGroups();
        if (response.getStatusCode() == 200) {
            model.addAttribute("majorsList", response.getOurMajor());
            return "major/themnganhhoc";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "major/themnganhhoc";
        }
    }
    @PostMapping("/admin/them-nganh")
    public String addMajor(@ModelAttribute MajorDetailDto majorDetailDto,
                           RedirectAttributes redirectAttributes) {
        MajorDetailDto response = majorDetailService.addMajor(majorDetailDto);

        if (response.getStatusCode() == 400 || response.getStatusCode() == 404) {
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            return "redirect:/admin/them-nganh";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thêm ngành thành công");
        return "redirect:/admin/them-nganh";
    }

    @GetMapping("/admin/danh-sach-nganh")
    public String getAllMajorDetail(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0) page = 0;
            Page<MajorDetails> majordetail = majorDetailService.getAllMajorDetails(page, size);
            model.addAttribute("majordetails", majordetail.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", majordetail.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách sự kiện: " + e.getMessage());
        }
        return "major/danhsachnganhhoc";
    }

    @PostMapping("/admin/xoa-nganh/{id}")
    public String deleteMajor(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            MajorDetailDto response = majorDetailService.deleteMajorGroup(id);
            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", "Xóa ngành thành công!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa ngành.");
        }
        return "redirect:/admin/danh-sach-nganh";
    }

    @GetMapping("/admin/chinh-sua-nganh/{id}")
    public String editMajorDetailForm(@PathVariable Integer id, Model model) {
        MajorDetails majordetails = majorDetailService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy nhóm ngành với ID: " + id));
        model.addAttribute("majordetails", majordetails);
        return "major/chinhsuanganhhoc"; // View chỉnh sửa nhóm ngành
    }

    @PostMapping("/admin/chinh-sua-nganh")
    public String updateMajorDetails(@ModelAttribute MajorDetailDto majorDetailDto, Model model) {
        MajorDetailDto response = majorDetailService.updateMajorDetails(majorDetailDto);
        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/admin/danh-sach-nganh";
    }
}
