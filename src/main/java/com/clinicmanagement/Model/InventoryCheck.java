package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "inventory_checks") // Đổi tên bảng
@Data
public class InventoryCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ----- GENERAL CHECK INFORMATION -----

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String title;

    private Integer lineCount; // Tổng số dòng sản phẩm trong phiếu kiểm kê chi tiết


    // ----- RELATIONSHIPS (FOREIGN KEYS) -----

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id", nullable = false)
    private Warehouse warehouse; // Đổi tên lớp và thuộc tính

    /**
     * The user who created this inventory check.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy; // Đổi tên thuộc tính

    /**
     * The employee responsible for verifying the check.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_id")
    private User verifiedBy; // Đổi tên thuộc tính


    // ----- RELATIONSHIP TO DETAIL TABLE -----

    /**
     * An inventory check has many detail lines.
     * When a check is deleted, its details are also deleted.
     * The `mappedBy` value "inventoryCheck" must match the field name in the `InventoryCheckDetail` class.
     */
    @OneToMany(mappedBy = "inventoryCheck", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryCheckDetail> details; // Đổi tên thuộc tính và lớp
}