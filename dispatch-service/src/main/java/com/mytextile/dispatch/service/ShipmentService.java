package com.mytextile.dispatch.service;

import com.mytextile.dispatch.dto.CreateShipmentRequestDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;

public interface ShipmentService {

    /**
     * Creates a new shipment (challan).
     * @param requestDto DTO containing shipment details
     * @return The created shipment
     */
    ShipmentResponseDto createShipment(CreateShipmentRequestDto requestDto);

    /**
     * Retrieves a shipment by its ID.
     * @param shipmentId The ID of the shipment
     * @return The found shipment
     */
    ShipmentResponseDto getShipmentById(Long shipmentId);
}