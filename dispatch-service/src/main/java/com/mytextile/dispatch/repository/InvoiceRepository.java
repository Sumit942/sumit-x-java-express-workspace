package com.mytextile.dispatch.repository;

import com.mytextile.dispatch.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    Optional<Invoice> findByShipment_ShipmentId(Long shipmentId);
}
