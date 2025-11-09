package com.mytextile.production.service;

import com.mytextile.production.dto.*;

public interface ProductionService {

    // --- Job Methods ---
    ProductionJobDto createProductionJob(CreateProductionJobRequestDto requestDto);
    ProductionJobDto startProductionJob(Long jobId);
    ProductionJobDto completeProductionJob(Long jobId, CompleteJobRequestDto requestDto);
    ProductionJobDto cancelProductionJob(Long jobId);

    // --- Machine Methods ---
    MachineDto updateMachine(Long machineId, MachineUpdateDto dto);
    void deleteMachine(Long machineId);
    
    // You would add these as well
    // MachineDto createMachine(CreateMachineDto createDto);
    // List<MachineDto> getAllMachines();
}