package com.mytextile.inventory.repository;

import com.mytextile.inventory.model.InventoryLedger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface InventoryLedgerRepository extends JpaRepository<InventoryLedger, Long> {

    List<InventoryLedger> findAllByItemId(Long itemId);

    @Query("SELECT COALESCE(SUM(l.quantity), 0) FROM InventoryLedger l WHERE l.itemId = :itemId")
    BigDecimal getStockByItemId(Long itemId);
}
