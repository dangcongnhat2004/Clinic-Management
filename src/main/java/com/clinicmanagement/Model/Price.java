package com.clinicmanagement.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "prices") // Đổi tên bảng
@Data
public class Price {

    @EmbeddedId
    private PriceId id; // Đổi tên lớp ID

    // ----- RELATIONSHIPS TO THE COMPOSITE KEY -----

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("medicineId") // Ánh xạ tới 'medicineId' trong lớp PriceId
    @JoinColumn(name = "medicine_id", insertable = false, updatable = false)
    private Medicine medicine; // Đổi tên lớp và thuộc tính

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("supplierId") // Ánh xạ tới 'supplierId' trong lớp PriceId
    @JoinColumn(name = "supplier_id", insertable = false, updatable = false)
    private Supplier supplier; // Đổi tên lớp và thuộc tính


    // ----- PRICE DATA FIELDS -----

    private String priceType; // e.g., "PURCHASE_PRICE", "RETAIL_PRICE"

    private LocalDate entryDate; // Ngày nhập

    private LocalDate effectiveStartDate; // Ngày bắt đầu hiệu lực

    private LocalDate effectiveEndDate; // Ngày kết thúc hiệu lực

    private String packagingSpec; // Quy cách, e.g., "Box of 10 blisters x 10 tablets"

    private String unitConversion; // Quy đổi, e.g., "1 Carton = 24 Boxes"

    @Column(precision = 19, scale = 4)
    private BigDecimal priceBeforeTax;

    private Double vat; // Stored as a decimal, e.g., 0.1 for 10%

    @Column(columnDefinition = "TEXT")
    private String notes;
}