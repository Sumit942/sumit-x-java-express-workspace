package com.mytextile.production.mapper;

import com.mytextile.production.dto.MachineDto;
import com.mytextile.production.dto.MachineUpdateDto;
import com.mytextile.production.model.Machine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface MachineMapper {

    MachineDto toDto(Machine machine);

    void updateEntityFromDto(@MappingTarget Machine machine, MachineUpdateDto updateDto);
}