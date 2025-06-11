package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_logs") // Đổi tên bảng sang tiếng Anh
@Data
public class InventoryLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ----- RELATIONSHIPS (FOREIGN KEYS) -----

    /**
     * Link to the shipment that caused this inventory change.
     * Can be null if it's from a manual adjustment or an inventory check.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id") // Đổi tên cột
    private Shipment shipment; // Đổi tên lớp và thuộc tính

    /**
     * The warehouse where the medicine is stored.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false) // Đổi tên cột
    private Warehouse warehouse; // Đổi tên lớp và thuộc tính

    /**
     * The medicine being logged.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicine_id", nullable = false) // Đổi tên cột
    private Medicine medicine; // Đổi tên lớp và thuộc tính


    // ----- INVENTORY LOG DATA FIELDS -----

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    /**
     * The quantity recorded in the system before the transaction/check.
     */
    private Long bookQuantity; // Số lượng sổ sách

    /**
     * The actual physical quantity counted.
     */
    private Long actualQuantity; // Số lượng thực tế

    /**
     * The difference between actual and book quantity (actualQuantity - bookQuantity).
     * > 0: Surplus
     * < 0: Shortage
     * = 0: Match
     */
    private Long discrepancy; // Thừa/Thiếu

    @Column(columnDefinition = "TEXT")
    private String notes;
}