package com.clinicmanagement.Model; // Bạn nên đổi tên package cho phù hợp, ví dụ: com.yourproject.model

import jakarta.persistence.*;
import lombok.Data;
import java.util.Set;

@Entity
@Table(name = "warehouses") // Đổi tên bảng sang tiếng Anh
@Data
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private String status; // e.g., "ACTIVE", "MAINTENANCE", "CLOSED"

    private String warehouseType; // e.g., "PRIMARY", "RETAIL", "SPECIALIZED"

    private String storedMedicineType; // e.g., "GENERAL", "VACCINE", "COLD_STORAGE"

    private String scope; // e.g., "INTERNAL", "EXTERNAL"

    private String department; // Managing department

    @Column(columnDefinition = "TEXT")
    private String description;


    // ----- ONE-TO-MANY RELATIONSHIPS -----

    /**
     * A warehouse can have many historical inventory logs.
     * The `mappedBy` value "warehouse" must match the field name in the `InventoryLog` class.
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<InventoryLog> inventoryLogs; // Đổi tên lớp và thuộc tính

    /**
     * A warehouse can have many inventory checks.
     * The `mappedBy` value "warehouse" must match the field name in the `InventoryCheck` class.
     */
    @OneToMany(mappedBy = "warehouse", cascade = CascadeType.ALL)
    private Set<InventoryCheck> inventoryChecks; // Đổi tên lớp và thuộc tính

    /**
     * A warehouse can be the SOURCE of many outgoing shipments.
     * The `mappedBy` value "sourceWarehouse" must match the field name in the `Shipment` class.
     */
    @OneToMany(mappedBy = "sourceWarehouse")
    private Set<Shipment> outgoingShipments; // Đổi tên lớp và thuộc tính

    /**
     * A warehouse can be the DESTINATION for many incoming shipments.
     * The `mappedBy` value "destinationWarehouse" must match the field name in the `Shipment` class.
     */
    @OneToMany(mappedBy = "destinationWarehouse")
    private Set<Shipment> incomingShipments; // Đổi tên lớp và thuộc tính
}