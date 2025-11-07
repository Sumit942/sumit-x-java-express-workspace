package com.mytextile.production.service.impl;

import com.mytextile.production.dto.ProductionJobDto;
import com.mytextile.production.entity.ProductionJob;
import com.mytextile.production.exception.ResourceNotFoundException;
import com.mytextile.production.mapper.ProductionJobMapper;
import com.mytextile.production.repository.ProductionJobRepository;
import com.mytextile.production.service.IProductionJobService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductionJobServiceImpl implements IProductionJobService {

    private final ProductionJobRepository jobRepository;
    private final ProductionJobMapper jobMapper;

    @Override
    public ProductionJobDto createJob(ProductionJobDto productionJob) {
        ProductionJob job = jobMapper.toEntity(productionJob);
        ProductionJob savedJob = jobRepository.save(job);
        return jobMapper.toDto(savedJob);
    }

    @Override
    public ProductionJobDto getJobById(Long jobId) {
        ProductionJob productionJob = jobRepository.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("productionJob", "id", jobId));
        return jobMapper.toDto(productionJob);
    }

    @Override
    public List<ProductionJobDto> getAllJobs() {
        return jobRepository.findAll()
                .stream()
                .map(jobMapper::toDto)
                .toList();
    }
}
