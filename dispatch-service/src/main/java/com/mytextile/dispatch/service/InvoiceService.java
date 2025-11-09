package com.mytextile.dispatch.service;

import com.mytextile.dispatch.dto.*;

public interface InvoiceService {
    InvoiceResponseDto createInvoice(CreateInvoiceRequestDto requestDto);
    InvoiceResponseDto getInvoiceById(Long invoiceId);
    InvoiceResponseDto getInvoiceForShipment(Long shipmentId);
    InvoiceResponseDto updateDraftInvoice(Long invoiceId, UpdateInvoiceDto updateDto);
    InvoiceResponseDto voidSentInvoice(Long invoiceId);
    InvoiceResponseDto sendInvoice(Long invoiceId);
}