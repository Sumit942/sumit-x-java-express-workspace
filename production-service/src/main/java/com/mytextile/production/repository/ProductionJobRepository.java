package com.mytextile.production.repository;

import com.mytextile.production.entity.ProductionJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductionJobRepository extends JpaRepository<ProductionJob, Long> {
}
