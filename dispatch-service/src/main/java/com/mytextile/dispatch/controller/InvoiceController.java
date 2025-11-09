package com.mytextile.dispatch.controller;

import com.mytextile.dispatch.dto.CreateInvoiceRequestDto;
import com.mytextile.dispatch.dto.InvoiceResponseDto;
import com.mytextile.dispatch.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<InvoiceResponseDto> createInvoice(
            @Valid @RequestBody CreateInvoiceRequestDto requestDto) {
        
        InvoiceResponseDto createdInvoice = invoiceService.createInvoice(requestDto);
        return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceById(@PathVariable("id") Long invoiceId) {
        InvoiceResponseDto invoiceDto = invoiceService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(invoiceDto);
    }

    @GetMapping("/by-shipment/{shipmentId}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByShipmentId(
            @PathVariable Long shipmentId) {
        
        InvoiceResponseDto invoiceDto = invoiceService.getInvoiceForShipment(shipmentId);
        return ResponseEntity.ok(invoiceDto);
    }
}