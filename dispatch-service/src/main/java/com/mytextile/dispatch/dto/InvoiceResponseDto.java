package com.mytextile.dispatch.dto;

import com.mytextile.dispatch.entity.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

// "Read" DTO for an invoice, sent as a response
public record InvoiceResponseDto(
    Long invoiceId,
    Long shipmentId, // Key link
    String invoiceNumber,
    BigDecimal amount,
    InvoiceStatus status,
    LocalDate issuedDate,
    LocalDate dueDate
) {}