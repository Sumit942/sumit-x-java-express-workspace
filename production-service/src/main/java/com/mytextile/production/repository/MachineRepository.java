package com.mytextile.production.repository;

import com.mytextile.production.model.Machine;
import com.mytextile.production.model.MachineStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MachineRepository extends JpaRepository<Machine, Long> {
    /**
     * Finds the first machine that has the given status.
     * This is more efficient than fetching all available machines.
     */
    Optional<Machine> findFirstByStatus(MachineStatus status);
}
