package com.mytextile.inventory.dto;

import com.mytextile.inventory.model.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record LedgerEntryDto(
    Long ledgerId,
    Long orderId,
    String sku,
    String itemName,
    BigDecimal quantity,
    TransactionType transactionType,
    LocalDateTime transactionDate
) {}