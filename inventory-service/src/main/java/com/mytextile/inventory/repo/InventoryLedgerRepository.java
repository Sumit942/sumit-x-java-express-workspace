package com.mytextile.inventory.repo;

import com.mytextile.inventory.entity.InventoryLedger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryLedgerRepository extends JpaRepository<InventoryLedger, Long> {
}
