package com.clinicmanagement.Model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "shipments") // Đổi tên bảng
@Data
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // ----- UPDATED AND NEW FIELDS -----

    @Column(unique = true)
    private String code; // Mã tự sinh theo logic, ví dụ: SHIP-2023-0001

    private String referenceCode; // Mã do người dùng nhập

    private String invoiceNumber; // Số hóa đơn

    private String externalReferenceNumber; // Số phiếu tham chiếu từ đối tác

    /**
     * Shipment type: e.g., "INBOUND", "OUTBOUND", "TRANSFER", "RETURN"
     * Suggestion: Use an Enum for better management.
     */
    private String type;

    /**
     * Shipment status: e.g., "DRAFT", "PENDING_APPROVAL", "APPROVED", "COMPLETED", "CANCELLED"
     * Suggestion: Use an Enum for better management.
     */
    private String status;

    private String scope; // e.g., "INTERNAL", "EXTERNAL"

    private String customerName; // For outbound sales shipments

    private LocalDate invoiceDate;

    private LocalDateTime createdAt;

    private LocalDateTime approvedAt;

    private LocalDateTime completedAt; // Ngày hàng vào/ra kho

    private Integer lineCount; // Total number of medicine lines in the details

    @Column(precision = 19, scale = 4)
    private BigDecimal totalValue;

    @Column(columnDefinition = "TEXT")
    private String notes;


    // ----- RELATIONSHIPS (FOREIGN KEYS) -----

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by_id")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "verified_by_id")
    private User verifiedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_warehouse_id") // Kho đi
    private Warehouse sourceWarehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_warehouse_id") // Kho đến
    private Warehouse destinationWarehouse;


    // ----- RELATIONSHIP TO DETAIL TABLE -----

    /**
     * A shipment has many detail lines.
     * When a shipment is deleted, its details are also deleted.
     * The `mappedBy` value "shipment" must match the field name in the `ShipmentDetail` class.
     */
    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ShipmentDetail> details;


    // ----- HELPER METHODS -----

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = "DRAFT"; // Set default status on creation
    }
}