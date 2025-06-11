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
public class ShipmentDetailId implements Serializable {

    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "medicine_id")
    private Long medicineId;
}