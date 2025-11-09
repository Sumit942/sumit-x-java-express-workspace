package com.mytextile.inventory.dto;

import java.math.BigDecimal;

public record StockLevelDto(
    Long itemId,
    String sku,
    String name,
    BigDecimal currentStock,
    String unit
) {}