package com.mytextile.production.dto;

import jakarta.validation.constraints.NotEmpty;

public record CreateMachineDto(
    @NotEmpty(message = "Machine name cannot be empty")
    String name
) {}