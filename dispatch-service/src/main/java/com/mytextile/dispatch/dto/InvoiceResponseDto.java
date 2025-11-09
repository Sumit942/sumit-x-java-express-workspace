package com.mytextile.dispatch.dto;
// (As provided before)
import com.mytextile.dispatch.model.InvoiceStatus;
import java.math.BigDecimal;
import java.time.LocalDate;
public record InvoiceResponseDto(
    Long invoiceId, Long shipmentId, String invoiceNumber,
    BigDecimal amount, InvoiceStatus status, LocalDate issuedDate, LocalDate dueDate
) {}