package com.example.admission.admissionswebsite.Controller;

import com.example.admission.admissionswebsite.Model.Appointment;
import com.example.admission.admissionswebsite.service.AppointmentService;
import com.example.admission.admissionswebsite.service.VnpayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Random;

@Controller
public class BookingController {
    @Autowired
    private VnpayService vnpayService;
    @Autowired
    private AppointmentService appointmentService; // Giả sử bạn có service này

    // THAY THẾ HOÀN TOÀN HÀM POST CUỐI CÙNG
    // Sử dụng constructor injection, đây là cách làm được khuyến nghị
    @Autowired
    public BookingController(VnpayService vnpayService, AppointmentService appointmentService) {
        this.vnpayService = vnpayService;
        this.appointmentService = appointmentService;
    }

    @PostMapping("/user/dat-lich/tao-thanh-toan-vnpay")
    public String createVnpayPayment(HttpServletRequest request,
                                     // ===========================================
                                     // SỬA ĐỔI QUAN TRỌNG: Đổi Integer -> Long
                                     // ===========================================
                                     @RequestParam("doctorId") Long doctorId,
                                     @RequestParam("appointmentDate") String date,
                                     @RequestParam("appointmentTime") String time,
                                     @RequestParam("appointmentType") Appointment.AppointmentType type,
                                     @RequestParam("reasonForVisit") String reason,
                                     @RequestParam("paymentMethod") String paymentMethod) {

        // Gọi service với kiểu Long, đảm bảo tính nhất quán
        Appointment newAppointment = appointmentService.createPendingAppointment(
                doctorId, date, time, type, reason, paymentMethod
        );

        BigDecimal fee = newAppointment.getFee();
        // Sửa nội dung thanh toán để parse ID dễ dàng hơn
        String orderInfo = "Thanh toan lich hen ID " + newAppointment.getId();
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayReturnUrl = baseUrl + "/vnpay-payment-return";

        String vnpayUrl = vnpayService.createOrder(fee, orderInfo, vnpayReturnUrl);

        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/user/vnpay-payment-return")
    public String handleVnpayReturn(HttpServletRequest request, Model model) {
        int paymentStatus = vnpayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        BigDecimal amount = new BigDecimal(totalPrice).divide(new BigDecimal("100"));

        if (paymentStatus == 1) { // Giao dịch thành công
            try {
                // Sửa lại cách parse ID cho an toàn hơn
                String[] parts = orderInfo.split(" ");
                Long appointmentId = Long.parseLong(parts[parts.length - 1]);
                appointmentService.confirmAppointmentPayment(appointmentId, transactionId);
            } catch (Exception e) {
                System.err.println("Lỗi nghiêm trọng: không thể parse appointmentId từ vnp_OrderInfo: " + orderInfo);
                // Có thể hiển thị một trang lỗi đặc biệt ở đây
            }

            model.addAttribute("orderId", orderInfo);
            model.addAttribute("totalPrice", amount.toPlainString());
            model.addAttribute("paymentTime", paymentTime);
            model.addAttribute("transactionId", transactionId);
            return "pay/payment_success";
        } else { // Giao dịch thất bại
            return "pay/payment_failure";
        }
    }
    public static String getIpAddress(HttpServletRequest request) {
        String ipAdress;
        try {
            ipAdress = request.getHeader("X-FORWARDED-FOR");
            if (ipAdress == null) {
                ipAdress = request.getRemoteAddr();
            }
        } catch (Exception e) {
            ipAdress = "Invalid IP:" + e.getMessage();
        }
        return ipAdress;
    }

    public static String getRandomNumber(int len) {
        Random rnd = new Random();
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(chars.charAt(rnd.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
