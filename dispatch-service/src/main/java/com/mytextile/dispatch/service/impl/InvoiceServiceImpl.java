package com.mytextile.dispatch.service.impl;

import com.mytextile.dispatch.dto.CreateInvoiceRequestDto;
import com.mytextile.dispatch.dto.InvoiceResponseDto;
import com.mytextile.dispatch.entity.Invoice;
import com.mytextile.dispatch.entity.InvoiceStatus;
import com.mytextile.dispatch.entity.Shipment;
import com.mytextile.dispatch.exception.ResourceNotFoundException;
import com.mytextile.dispatch.mapper.InvoiceMapper;
import com.mytextile.dispatch.repository.InvoiceRepository;
import com.mytextile.dispatch.repository.ShipmentRepository;
import com.mytextile.dispatch.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final ShipmentRepository shipmentRepository;
    private final InvoiceMapper invoiceMapper;
    // Inject Kafka/RabbitMQ producer
    // private final KafkaTemplate<String, InvoiceCreatedEvent> kafkaTemplate;


    @Override
    public InvoiceResponseDto createInvoice(CreateInvoiceRequestDto requestDto) {
        log.info("Creating invoice for shipment ID: {}", requestDto.shipmentId());

        // 1. Find the parent Shipment
        Shipment shipment = shipmentRepository.findById(requestDto.shipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", requestDto.shipmentId()));

        // TODO: Add logic here to prevent creating multiple invoices for one shipment
        
        // 2. Create new Invoice entity
        Invoice invoice = new Invoice();
        invoice.setShipment(shipment);
        invoice.setAmount(requestDto.amount());
        
        // 3. Apply Business Logic
        invoice.setInvoiceNumber(generateInvoiceNumber(shipment.getOrderId()));
        invoice.setStatus(InvoiceStatus.DRAFT); // Or SENT if it's emailed immediately
        invoice.setIssuedDate(LocalDate.now());
        invoice.setDueDate(LocalDate.now().plusDays(30)); // Assuming 30-day terms

        // 4. Save Entity
        Invoice savedInvoice = invoiceRepository.save(invoice);

        // 5. Publish Event
        log.info("Publishing INVOICE_CREATED event for order {}", shipment.getOrderId());
        // kafkaTemplate.send("invoice_created_topic", new InvoiceCreatedEvent(...));

        // 6. Map to DTO for response
        return invoiceMapper.toDto(savedInvoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceById(Long invoiceId) {
        log.info("Fetching invoice with ID: {}", invoiceId);
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", invoiceId));
        
        return invoiceMapper.toDto(invoice);
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceForShipment(Long shipmentId) {
        log.info("Fetching invoice for shipment ID: {}", shipmentId);
        // Use the custom method we added to the repository
        Invoice invoice = invoiceRepository.findByShipment_ShipmentId(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "shipmentId", shipmentId));
        
        return invoiceMapper.toDto(invoice);
    }

    // --- Helper Methods ---
    
    private String generateInvoiceNumber(Long orderId) {
        // Example: INV-12345-EFG8
        String shortUuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("INV-%s-%s", orderId, shortUuid);
    }
}