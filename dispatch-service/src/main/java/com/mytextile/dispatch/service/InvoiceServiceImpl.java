package com.mytextile.dispatch.service;

import com.mytextile.dispatch.dto.*;
import com.mytextile.dispatch.exception.BusinessLogicException;
import com.mytextile.dispatch.exception.ResourceNotFoundException;
import com.mytextile.dispatch.mapper.InvoiceMapper;
import com.mytextile.dispatch.model.Invoice;
import com.mytextile.dispatch.model.InvoiceStatus;
import com.mytextile.dispatch.model.Shipment;
import com.mytextile.dispatch.repository.InvoiceRepository;
import com.mytextile.dispatch.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

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
    private final RestTemplate restTemplate;

    @Value("${app.client-service.url}")
    private String clientServiceUrl;

    @Value("${app.notification-service.url}")
    private String notificationServiceUrl;

    @Override
    public InvoiceResponseDto createInvoice(CreateInvoiceRequestDto requestDto) {
        log.info("Creating invoice for shipment ID: {}", requestDto.shipmentId());

        Shipment shipment = shipmentRepository.findById(requestDto.shipmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Shipment", "id", requestDto.shipmentId()));

        if (invoiceRepository.findByShipment_ShipmentId(requestDto.shipmentId()).isPresent()) {
            throw new BusinessLogicException("Invoice for shipment " + requestDto.shipmentId() + " already exists.");
        }

        Invoice invoice = new Invoice();
        invoice.setShipment(shipment);
        invoice.setAmount(requestDto.amount());
        invoice.setInvoiceNumber(generateInvoiceNumber(shipment.getOrderId()));
        invoice.setStatus(InvoiceStatus.DRAFT);
        invoice.setDueDate(LocalDate.now().plusDays(30));

        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(savedInvoice);
    }

    @Override
    public InvoiceResponseDto sendInvoice(Long invoiceId) {
        log.info("Attempting to send invoice: {}", invoiceId);
        Invoice invoice = findInvoiceById(invoiceId);

        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new BusinessLogicException("Only DRAFT invoices can be sent. Current status: " + invoice.getStatus());
        }

        Shipment shipment = invoice.getShipment();
        Long clientId = shipment.getClientId();
        
        // 1. Call Client Service
        String clientUrl = String.format("%s/api/v1/clients/%d", clientServiceUrl, clientId);
        ClientDataDto client = getClientData(clientUrl);

        // 2. Build Notification Request
        String subject = String.format("Invoice Ready: %s from My Textile Company", invoice.getInvoiceNumber());
        String body = String.format("Dear %s,\n\nYour invoice %s for the amount of %s is ready.\n",
                client.name(), invoice.getInvoiceNumber(), invoice.getAmount().toString());

        NotificationRequestDto notificationRequest = new NotificationRequestDto(
                clientId, "EMAIL", client.email(), subject, body);

        // 3. Call Notification Service
        String notifyUrl = String.format("%s/api/v1/notifications/send", notificationServiceUrl);
        sendNotification(notifyUrl, notificationRequest);

        // 4. Update Invoice Status
        invoice.setStatus(InvoiceStatus.SENT);
        invoice.setIssuedDate(LocalDate.now());
        Invoice savedInvoice = invoiceRepository.save(invoice);

        return invoiceMapper.toDto(savedInvoice);
    }
    
    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceById(Long invoiceId) {
        return invoiceMapper.toDto(findInvoiceById(invoiceId));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponseDto getInvoiceForShipment(Long shipmentId) {
        Invoice invoice = invoiceRepository.findByShipment_ShipmentId(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "shipmentId", shipmentId));
        return invoiceMapper.toDto(invoice);
    }

    @Override
    public InvoiceResponseDto updateDraftInvoice(Long invoiceId, UpdateInvoiceDto updateDto) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (invoice.getStatus() != InvoiceStatus.DRAFT) {
            throw new BusinessLogicException("Only DRAFT invoices can be updated.");
        }
        
        invoice.setAmount(updateDto.amount());
        invoice.setDueDate(updateDto.dueDate());
        Invoice savedInvoice = invoiceRepository.save(invoice);
        return invoiceMapper.toDto(savedInvoice);
    }

    @Override
    public InvoiceResponseDto voidSentInvoice(Long invoiceId) {
        Invoice invoice = findInvoiceById(invoiceId);
        if (invoice.getStatus() == InvoiceStatus.PAID) {
            throw new BusinessLogicException("Cannot void a PAID invoice.");
        }
        if (invoice.getStatus() == InvoiceStatus.DRAFT) {
            throw new BusinessLogicException("Cannot void a DRAFT invoice. Delete it instead.");
        }

        invoice.setStatus(InvoiceStatus.VOIDED);
        Invoice savedInvoice = invoiceRepository.save(invoice);
        // TODO: Publish an InvoiceVoidedEvent
        return invoiceMapper.toDto(savedInvoice);
    }
    
    // --- Private Helper Methods ---

    private Invoice findInvoiceById(Long invoiceId) {
        return invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", invoiceId));
    }
    
    private String generateInvoiceNumber(Long orderId) {
        String shortUuid = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("INV-%s-%s", orderId, shortUuid);
    }

    private ClientDataDto getClientData(String url) {
        try {
            ClientDataDto client = restTemplate.getForObject(url, ClientDataDto.class);
            if (client == null || client.email() == null) {
                throw new BusinessLogicException("Client data is missing or email is null.");
            }
            return client;
        } catch (Exception e) {
            log.error("Failed to fetch client data. Error: {}", e.getMessage());
            throw new BusinessLogicException("Could not retrieve client email. Aborting send.");
        }
    }
    
    private void sendNotification(String url, NotificationRequestDto request) {
        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            log.info("Notification service accepted request. Status: {}", response.getStatusCode());
        } catch (Exception e) {
            log.error("Failed to send notification request. Error: {}", e.getMessage());
            // Do not fail the transaction, just log the error
        }
    }
}