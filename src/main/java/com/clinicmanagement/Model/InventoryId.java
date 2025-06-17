package com.clinicmanagement.Model; // Bạn nên đặt các lớp ID vào một package con là 'embedded'

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryId implements Serializable {

    @Column(name = "warehouse_id")
    private Long warehouseId;

    @Column(name = "medicine_id")
    private Long medicineId;

    // Lombok's @Data annotation automatically generates the required
    // equals() and hashCode() methods, which are essential for composite keys.
}