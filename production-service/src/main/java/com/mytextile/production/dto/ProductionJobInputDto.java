package com.mytextile.production.dto;

import java.math.BigDecimal;

public record ProductionJobInputDto(
    Long jobInputId,
    String inputItemSku,
    BigDecimal quantity
) {}