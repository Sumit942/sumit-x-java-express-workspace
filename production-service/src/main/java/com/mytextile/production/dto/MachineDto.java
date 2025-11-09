package com.mytextile.production.dto;

import com.mytextile.production.model.MachineStatus;

public record MachineDto(
    Long machineId,
    String name,
    MachineStatus status
) {}