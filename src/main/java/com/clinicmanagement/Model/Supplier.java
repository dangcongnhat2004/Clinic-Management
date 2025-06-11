package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "suppliers") // Đổi tên bảng sang tiếng Anh
@Data
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String name;

    private String type; // e.g., "INDIVIDUAL", "COMPANY", "DISTRIBUTOR"

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(unique = true)
    private String taxCode;

    private String director; // Director's name / legal representative

    private String fax;

    private String contactPerson; // Direct contact person's name

    private String phoneNumber;

    @Column(unique = true)
    private String email;

    private String status; // e.g., "ACTIVE", "INACTIVE"

    @Column(columnDefinition = "TEXT")
    private String description;


    // ----- ONE-TO-MANY RELATIONSHIPS -----

    /**
     * A supplier can provide goods for many shipments.
     * The `mappedBy` value "supplier" must match the field name in the `Shipment` class.
     */
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<Shipment> shipments; // Đổi tên lớp và thuộc tính

    /**
     * A supplier can have multiple prices for different products.
     * The `mappedBy` value "supplier" must match the field name in the `Price` class.
     */
    @OneToMany(mappedBy = "supplier", fetch = FetchType.LAZY)
    private Set<Price> prices; // Đổi tên lớp và thuộc tính
}