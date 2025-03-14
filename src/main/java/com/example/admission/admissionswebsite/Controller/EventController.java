package com.example.admission.admissionswebsite.Controller;


import com.example.admission.admissionswebsite.Dto.EventDto;
import com.example.admission.admissionswebsite.Dto.UserDto;
import com.example.admission.admissionswebsite.Model.AdminPost;
import com.example.admission.admissionswebsite.Model.Event;
import com.example.admission.admissionswebsite.service.AdminService;
import com.example.admission.admissionswebsite.service.EventService;
import com.example.admission.admissionswebsite.service.UniversityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private AdminService adminService;
    @GetMapping("/admin/them-su-kien")
    public String showAddEventForm(Model model) {
        UserDto response = adminService.getUserIdsByUniversityRole();
        if (response.getStatusCode() == 200) {
            model.addAttribute("usersList", response.getOurUser());
            return "event/themsukien";
        } else {
            model.addAttribute("errorMessage", response.getMessage());
            return "404";
        }

    }

    @PostMapping("/admin/them-su-kien")
    public String addEvent(@ModelAttribute EventDto eventDto,
                           @RequestParam("imageEvent") MultipartFile image,
                           RedirectAttributes redirectAttributes) {
        if (image == null || image.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn hình ảnh sự kiện");
            return "redirect:/admin/them-su-kien";
        }

        EventDto response = eventService.addEvent(eventDto, image);
        if (response.getStatusCode() == 400) {
            redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            return "redirect:/admin/them-su-kien";
        }

        redirectAttributes.addFlashAttribute("successMessage", "Thêm sự kiện thành công");
        return "redirect:/admin/them-su-kien";
    }


    @GetMapping("/admin/danh-sach-su-kien")
    public String getAllEvents(Model model,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0) page = 0;
            Page<Event> eventPage = eventService.getAllEvents(page, size);
            model.addAttribute("events", eventPage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", eventPage.getTotalPages());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi khi lấy danh sách sự kiện: " + e.getMessage());
        }
        return "event/danhsachsukien";
    }

    @PostMapping("/admin/xoa-su-kien/{id}")
    public String deleteEvent(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            EventDto response = eventService.deleteEvent(id);
            if (response.getStatusCode() == 200) {
                redirectAttributes.addFlashAttribute("successMessage", response.getMessage());
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", response.getMessage());
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Đã xảy ra lỗi khi xóa sự kiện.");
        }
        return "redirect:/admin/danh-sach-su-kien";
    }

    @GetMapping("/admin/chinh-sua-su-kien/{id}")
    public String showEditEventForm(@PathVariable Integer id, Model model) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id));
        model.addAttribute("event", event);
        return "event/chinhsuasukien";
    }

    @PostMapping("/admin/cap-nhat-su-kien")
    public String capNhatSuKien(@ModelAttribute EventDto eventDto, Model model) {
        EventDto response = eventService.updateEvent(eventDto);
        if (response.getStatusCode() == 200) {
            model.addAttribute("successMessage", response.getMessage());
        } else {
            model.addAttribute("errorMessage", response.getMessage());
        }
        return "redirect:/admin/danh-sach-su-kien";
    }

    @GetMapping("/chi-tiet-su-kien/{id}")
    public String showEventDetails(@PathVariable Integer id, Model model) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy sự kiện với ID: " + id));
        model.addAttribute("event", event);
        return "/user/eventdetail";
    }



}
