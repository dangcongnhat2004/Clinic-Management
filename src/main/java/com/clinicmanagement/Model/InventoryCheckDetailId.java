package com.clinicmanagement.Model;


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
public class InventoryCheckDetailId implements Serializable {

    @Column(name = "inventory_check_id")
    private Long inventoryCheckId;

    @Column(name = "medicine_id")
    private Long medicineId;
}