package com.mytextile.orders.dto;

import com.mytextile.orders.model.OrderStatus;
import java.time.LocalDateTime;

public record OrderDto(
    Long orderId,
    Long clientId,
    String orderDescription,
    OrderStatus status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {}