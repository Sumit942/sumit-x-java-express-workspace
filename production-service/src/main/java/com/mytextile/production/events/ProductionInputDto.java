package com.mytextile.production.events;

import java.math.BigDecimal;

public record ProductionInputDto(
    String inputItemSku,
    BigDecimal quantityConsumed
) {}