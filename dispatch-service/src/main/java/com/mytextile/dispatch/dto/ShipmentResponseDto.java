package com.mytextile.dispatch.dto;
// (As provided before)
import com.mytextile.dispatch.model.ShipmentStatus;
import java.time.LocalDateTime;
import java.util.List;
public record ShipmentResponseDto(
    Long shipmentId, Long orderId, Long clientId, String challanNumber,
    String shippingAddress, LocalDateTime dispatchDate, ShipmentStatus status,
    List<ShipmentItemDto> items
) {}