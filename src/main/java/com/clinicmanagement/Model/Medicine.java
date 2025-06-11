package com.clinicmanagement.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "medicines") // Đổi tên bảng sang tiếng Anh
@Data
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ----- BASIC MEDICINE INFORMATION -----

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String originalBarcode;

    private String internalBarcode1;
    private String internalBarcode2;
    private String dosage; // Hàm lượng
    private String medicineGroup; // Nhóm dược
    private String registrationNumber; // Số Visa
    private LocalDate licenseExpiryDate; // Hạn Visa
    private String packaging; // Hình thức đóng gói
    private String unit; // Đơn vị tính (e.g., Tablet, Bottle, Box)

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String effects; // Tác dụng

    @Column(columnDefinition = "TEXT")
    private String indication; // Chỉ định

    @Column(columnDefinition = "TEXT")
    private String otherInfo; // Thông tin khác


    // ----- RELATIONSHIPS -----

    /**
     * Each medicine is produced by one manufacturer.
     * This is a Many-to-One relationship.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id") // Đổi tên cột khóa ngoại
    private Manufacturer manufacturer; // Đổi tên lớp và thuộc tính

    /**
     * A medicine can appear in many shipment details.
     * The `mappedBy` value "medicine" must match the field name in the `ShipmentDetail` class.
     */
    @OneToMany(mappedBy = "medicine") // Đổi mappedBy
    private Set<ShipmentDetail> shipmentDetails; // Đổi tên lớp và thuộc tính

    /**
     * A medicine can appear in many inventory check details.
     */
    @OneToMany(mappedBy = "medicine") // Đổi mappedBy
    private Set<InventoryCheckDetail> inventoryCheckDetails; // Đổi tên lớp và thuộc tính

    /**
     * A medicine can have many historical inventory logs.
     */
    @OneToMany(mappedBy = "medicine") // Đổi mappedBy
    private Set<InventoryLog> inventoryLogs; // Đổi tên lớp và thuộc tính

    /**
     * A medicine can have multiple prices from different suppliers.
     */
    @OneToMany(mappedBy = "medicine") // Đổi mappedBy
    private Set<Price> prices; // Đổi tên lớp và thuộc tính
}