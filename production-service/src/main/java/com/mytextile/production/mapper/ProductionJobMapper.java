package com.mytextile.production.mapper;

import com.mytextile.production.dto.CreateProductionJobInputDto;
import com.mytextile.production.dto.ProductionJobDto;
import com.mytextile.production.dto.ProductionJobInputDto;
import com.mytextile.production.model.Machine;
import com.mytextile.production.model.ProductionJob;
import com.mytextile.production.model.ProductionJobInput;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductionJobMapper {

    ProductionJobInputDto toInputDto(ProductionJobInput input);

    @Mapping(target = "machineName", source = "machine")
    ProductionJobDto toDto(ProductionJob job);

    ProductionJobInput toInputEntity(CreateProductionJobInputDto inputDto);
    
    default String fromMachine(Machine machine) {
        return (machine != null) ? machine.getName() : null;
    }
}