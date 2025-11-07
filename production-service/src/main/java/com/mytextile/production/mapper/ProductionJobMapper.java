package com.mytextile.production.mapper;

import com.mytextile.production.dto.ProductionJobDto;
import com.mytextile.production.dto.ProductionJobInputDto;
import com.mytextile.production.entity.ProductionJob;
import com.mytextile.production.entity.ProductionJobInput;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

@Component
public class ProductionJobMapper {

    public ProductionJobInputDto toDto(ProductionJobInput jobInput) {
        if (Objects.isNull(jobInput)) {
            return null;
        }

        return new ProductionJobInputDto(
                jobInput.getJobInputId(),
                jobInput.getProductionJob(),
                jobInput.getInputItemSku(),
                jobInput.getQuantity()
        );
    }

    public ProductionJobDto toDto(ProductionJob job) {
        if (Objects.isNull(job)) {
            return null;
        }

        List<ProductionJobInputDto> jobInputDtoList = null;
        if (!CollectionUtils.isEmpty(job.getInputs())) {
            jobInputDtoList = job.getInputs()
                    .stream()
                    .map(this::toDto)
                    .toList();
        }

        return new ProductionJobDto(
                job.getJobId(),
                job.getOrderId(),
                job.getMachine(),
                jobInputDtoList,
                job.getOutputItemSku(),
                job.getActualOutputQuantity(),
                job.getStatus()
        );
    }

    public ProductionJob toEntity(ProductionJobDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }

        List<ProductionJobInput> inputList = null;
        if (!CollectionUtils.isEmpty(dto.inputs())) {
            inputList = dto.inputs()
                    .stream()
                    .map(this::toJobInputEntity)
                    .toList();
        }

        ProductionJob job = new ProductionJob();
        job.setMachine(dto.machine());
        job.setInputs(inputList);
        job.setStatus(dto.status());
        job.setOrderId(dto.orderId());
        job.setOutputItemSku(dto.outputItemSku());
        job.setActualOutputQuantity(dto.actualOutputQuantity());

        return job;
    }

    public ProductionJobInput toJobInputEntity(ProductionJobInputDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        ProductionJobInput jobInput = new ProductionJobInput();
        jobInput.setProductionJob(dto.productionJob());
        jobInput.setInputItemSku(dto.inputItemSku());
        jobInput.setQuantity(dto.quantity());

        return jobInput;
    }
}
