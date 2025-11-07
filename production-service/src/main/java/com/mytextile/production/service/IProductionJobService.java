package com.mytextile.production.service;

import com.mytextile.production.dto.ProductionJobDto;
import com.mytextile.production.entity.ProductionJob;

import java.util.List;

public interface IProductionJobService {

    ProductionJobDto createJob(ProductionJobDto productionJob);
    ProductionJobDto getJobById(Long jobId);
    List<ProductionJobDto> getAllJobs();
}
