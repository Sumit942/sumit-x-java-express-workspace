package com.mytextile.production.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateProductionJobRequestDto(
    @NotNull(message = "Order ID cannot be null")
    Long orderId,

    @NotEmpty(message = "Output SKU cannot be empty")
    String outputItemSku,

    @Valid
    @NotEmpty(message = "Job must have at least one input")
    List<CreateProductionJobInputDto> inputs
) {}