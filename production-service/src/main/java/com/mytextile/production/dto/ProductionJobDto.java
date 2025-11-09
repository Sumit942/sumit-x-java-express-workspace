package com.mytextile.production.dto;

import com.mytextile.production.model.JobStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ProductionJobDto(
    Long jobId,
    Long orderId,
    String machineName,
    String outputItemSku,
    BigDecimal actualOutputQuantity,
    JobStatus status,
    LocalDateTime createdAt,
    LocalDateTime completedAt,
    List<ProductionJobInputDto> inputs
) {}