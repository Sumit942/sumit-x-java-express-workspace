package com.mytextile.dispatch.service;

import com.mytextile.dispatch.dto.CreateShipmentRequestDto;
import com.mytextile.dispatch.dto.ShipmentResponseDto;

public interface ShipmentService {
    ShipmentResponseDto createShipment(CreateShipmentRequestDto requestDto);
    ShipmentResponseDto getShipmentById(Long shipmentId);
}