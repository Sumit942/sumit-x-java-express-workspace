package com.mytextile.production.controller;

import com.mytextile.production.dto.ProductionJobDto;
import com.mytextile.production.service.IProductionJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/jobs")
@RequiredArgsConstructor
public class ProductionJobController {

    private final IProductionJobService jobService;

    @PostMapping
    public ResponseEntity<ProductionJobDto> createJob(@Valid @RequestBody ProductionJobDto jobDto) {
        ProductionJobDto createdJob = jobService.createJob(jobDto);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductionJobDto> getJobById(@Valid @RequestBody Long jobId) {
        ProductionJobDto job = jobService.getJobById(jobId);
        return ResponseEntity.ok(job);
    }

    @GetMapping
    public ResponseEntity<List<ProductionJobDto>> getAllJobs() {
        List<ProductionJobDto> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
}
