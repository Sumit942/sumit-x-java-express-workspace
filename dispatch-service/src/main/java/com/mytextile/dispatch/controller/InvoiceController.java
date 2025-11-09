package com.mytextile.dispatch.controller;

import com.mytextile.dispatch.dto.CreateInvoiceRequestDto;
import com.mytextile.dispatch.dto.InvoiceResponseDto;
import com.mytextile.dispatch.dto.UpdateInvoiceDto;
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
        return ResponseEntity.ok(invoiceService.getInvoiceById(invoiceId));
    }

    @GetMapping("/by-shipment/{shipmentId}")
    public ResponseEntity<InvoiceResponseDto> getInvoiceByShipmentId(
            @PathVariable Long shipmentId) {
        
        return ResponseEntity.ok(invoiceService.getInvoiceForShipment(shipmentId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceResponseDto> updateDraftInvoice(
            @PathVariable("id") Long invoiceId,
            @Valid @RequestBody UpdateInvoiceDto updateDto) {
        
        InvoiceResponseDto updatedInvoice = invoiceService.updateDraftInvoice(invoiceId, updateDto);
        return ResponseEntity.ok(updatedInvoice);
    }

    @PostMapping("/{id}/send")
    public ResponseEntity<InvoiceResponseDto> sendInvoice(@PathVariable("id") Long invoiceId) {
        InvoiceResponseDto sentInvoice = invoiceService.sendInvoice(invoiceId);
        return ResponseEntity.ok(sentInvoice);
    }

    @PostMapping("/{id}/void")
    public ResponseEntity<InvoiceResponseDto> voidSentInvoice(
            @PathVariable("id") Long invoiceId) {
        
        InvoiceResponseDto voidedInvoice = invoiceService.voidSentInvoice(invoiceId);
        return ResponseEntity.ok(voidedInvoice);
    }
}