package com.mytextile.dispatch.repository;

import com.mytextile.dispatch.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
