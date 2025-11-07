package com.mytextile.production.dto;

import com.mytextile.production.entity.ProductionJob;

import java.math.BigDecimal;

public record ProductionJobInputDto(
        Long jobInputId,
        ProductionJob productionJob,
        String inputItemSku,
        BigDecimal quantity
) {}
