package com.mytextile.production.dto;

import com.mytextile.production.model.MachineStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record MachineUpdateDto(
    @NotEmpty(message = "Machine name cannot be empty")
    String name,

    @NotNull(message = "Machine status cannot be null")
    MachineStatus status
) {}