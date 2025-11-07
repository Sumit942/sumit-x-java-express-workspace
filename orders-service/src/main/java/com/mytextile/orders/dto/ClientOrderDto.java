package com.mytextile.orders.dto;

import com.mytextile.orders.entity.OrderStatus;

public record ClientOrderDto(
        Long orderId,

        Long clientId,

        String orderDescription,

        OrderStatus status
) {
}
