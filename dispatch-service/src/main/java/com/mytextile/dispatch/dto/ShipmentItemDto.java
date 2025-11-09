package com.mytextile.dispatch.dto;

import java.math.BigDecimal;

public record ShipmentItemDto(
        Long itemId,

        String itemSku,

        BigDecimal quantity
) {
}
