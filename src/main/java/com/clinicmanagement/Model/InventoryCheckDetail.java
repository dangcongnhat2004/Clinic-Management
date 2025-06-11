package com.clinicmanagement.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "inventory_check_details") // Đổi tên bảng
@Data
public class InventoryCheckDetail {

    @EmbeddedId
    private InventoryCheckDetailId id; // Đổi tên lớp ID

    // ----- RELATIONSHIPS TO THE COMPOSITE KEY -----

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("inventoryCheckId") // Ánh xạ tới 'inventoryCheckId' trong lớp ID
    @JoinColumn(name = "inventory_check_id", insertable = false, updatable = false)
    private InventoryCheck inventoryCheck; // Đổi tên lớp và thuộc tính

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicineId") // Ánh xạ tới 'medicineId' trong lớp ID
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    private Medicine medicine; // Đổi tên lớp và thuộc tính


    // ----- DETAIL DATA FIELDS -----

    /**
     * Medicine name at the time of the check (snapshot data).
     */
    private String medicineName;

    /**
     * The quantity recorded in the system before the check.
     * This field is MANDATORY to calculate the discrepancy.
     */
    private Long bookQuantity; // Số lượng sổ sách

    /**
     * The actual physical quantity counted in the warehouse.
     */
    private Long actualQuantity; // Số lượng thực tế

    /**
     * The difference: actualQuantity - bookQuantity.
     */
    private Long discrepancy; // Thừa/Thiếu (Chênh lệch)

    /**
     * The quantity of damaged, unusable items.
     */
    private Long damagedQuantity;

    /**
     * The reason for the damage, if any.
     */
    private String damageReason;
}