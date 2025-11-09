package com.mytextile.dispatch.service;

import com.mytextile.dispatch.dto.CreateInvoiceRequestDto;
import com.mytextile.dispatch.dto.InvoiceResponseDto;

public interface InvoiceService {
    /**
     * Creates a new invoice for an existing shipment.
     * @param requestDto DTO containing shipment ID and amount
     * @return The created invoice
     */
    InvoiceResponseDto createInvoice(CreateInvoiceRequestDto requestDto);

    /**
     * Retrieves an invoice by its ID.
     * @param invoiceId The ID of the invoice
     * @return The found invoice
     */
    InvoiceResponseDto getInvoiceById(Long invoiceId);

    /**
     * Retrieves the invoice associated with a specific shipment.
     * @param shipmentId The ID of the shipment
     * @return The found invoice
     */
    InvoiceResponseDto getInvoiceForShipment(Long shipmentId);
}
