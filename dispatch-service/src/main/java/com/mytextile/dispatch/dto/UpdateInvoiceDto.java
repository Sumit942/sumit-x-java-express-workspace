package com.mytextile.dispatch.dto;
// (As provided before)
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
public record UpdateInvoiceDto(
    @NotNull @Positive BigDecimal amount,
    @NotNull LocalDate dueDate
) {}