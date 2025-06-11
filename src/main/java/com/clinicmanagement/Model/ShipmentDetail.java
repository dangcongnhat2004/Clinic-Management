package com.clinicmanagement.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "shipment_details") // Đổi tên bảng
@Data
public class ShipmentDetail {

    @EmbeddedId
    private ShipmentDetailId id; // Đổi tên lớp ID

    // ----- RELATIONSHIPS TO THE COMPOSITE KEY -----

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shipmentId") // Ánh xạ tới 'shipmentId' trong lớp ID
    @JoinColumn(name = "shipment_id", insertable = false, updatable = false)
    private Shipment shipment; // Đổi tên lớp và thuộc tính

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicineId") // Ánh xạ tới 'medicineId' trong lớp ID
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    private Medicine medicine; // Đổi tên lớp và thuộc tính


    // ----- MEDICINE INFORMATION SNAPSHOT FIELDS -----
    // These fields are copied from Medicine to ensure historical data integrity.

    private String medicineName;
    private String originalBarcode;
    private String internalBarcode1;
    private String internalBarcode2;
    private String dosage;
    private String medicineGroup;
    private String registrationNumber; // Số Visa
    private LocalDate licenseExpiryDate; // Hạn Visa
    private String manufacturerName; // Manufacturer name at the time of transaction
    private String packaging;
    private String unit; // e.g., Tablet, Bottle, Box

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(columnDefinition = "TEXT")
    private String effects;

    @Column(columnDefinition = "TEXT")
    private String indication;

    @Column(columnDefinition = "TEXT")
    private String otherInfo;


    // ----- TRANSACTIONAL DATA FIELDS (IMPORTANT) -----
    // These fields are specific to this particular shipment line.

    private Integer quantity;

    @Column(precision = 19, scale = 4)
    private BigDecimal unitPrice;

    @Column(precision = 19, scale = 4)
    private BigDecimal lineTotal;

    private String lotNumber; // Số lô sản xuất

    private LocalDate expiryDate; // Hạn dùng của lô hàng này

    @Column(columnDefinition = "TEXT")
    private String notes;
}