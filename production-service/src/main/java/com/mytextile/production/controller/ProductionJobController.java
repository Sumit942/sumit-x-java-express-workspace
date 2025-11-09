package com.mytextile.production.controller;

import com.mytextile.production.dto.CompleteJobRequestDto;
import com.mytextile.production.dto.CreateProductionJobRequestDto;
import com.mytextile.production.dto.ProductionJobDto;
import com.mytextile.production.service.ProductionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/production-jobs")
@RequiredArgsConstructor
public class ProductionJobController {

    private final ProductionService productionService;

    @PostMapping
    public ResponseEntity<ProductionJobDto> createProductionJob(
            @Valid @RequestBody CreateProductionJobRequestDto requestDto) {
        
        ProductionJobDto createdJob = productionService.createProductionJob(requestDto);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/start")
    public ResponseEntity<ProductionJobDto> startProductionJob(
            @PathVariable("id") Long jobId) {
        
        ProductionJobDto startedJob = productionService.startProductionJob(jobId);
        return ResponseEntity.ok(startedJob);
    }

    @PostMapping("/{id}/complete")
    public ResponseEntity<ProductionJobDto> completeProductionJob(
            @PathVariable("id") Long jobId,
            @Valid @RequestBody CompleteJobRequestDto requestDto) {
        
        ProductionJobDto completedJob = productionService.completeProductionJob(jobId, requestDto);
        return ResponseEntity.ok(completedJob);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<ProductionJobDto> cancelProductionJob(
            @PathVariable("id") Long jobId) {
        
        ProductionJobDto cancelledJob = productionService.cancelProductionJob(jobId);
        return ResponseEntity.ok(cancelledJob);
    }
}