package com.mytextile.production.controller;

import com.mytextile.production.dto.CreateMachineDto;
import com.mytextile.production.dto.MachineDto;
import com.mytextile.production.dto.MachineUpdateDto;
import com.mytextile.production.service.ProductionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/machines")
@RequiredArgsConstructor
public class MachineController {

    private final ProductionService productionService;

    // You must implement createMachine in your service layer
    @PostMapping
    public ResponseEntity<MachineDto> createMachine(
            @Valid @RequestBody CreateMachineDto createDto) {
        // MachineDto newMachine = productionService.createMachine(createDto);
        // return new ResponseEntity<>(newMachine, HttpStatus.CREATED);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MachineDto> updateMachine(
            @PathVariable("id") Long machineId,
            @Valid @RequestBody MachineUpdateDto updateDto) {
        
        MachineDto updatedMachine = productionService.updateMachine(machineId, updateDto);
        return ResponseEntity.ok(updatedMachine);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMachine(@PathVariable("id") Long machineId) {
        productionService.deleteMachine(machineId);
        return ResponseEntity.noContent().build();
    }
    
    // You must implement getAllMachines in your service layer
    @GetMapping
    public ResponseEntity<?> getAllMachines() {
        // List<MachineDto> machines = productionService.getAllMachines();
        // return ResponseEntity.ok(machines);
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }
}