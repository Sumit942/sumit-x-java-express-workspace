package com.mytextile.dispatch.mapper;

import com.mytextile.dispatch.dto.InvoiceResponseDto;
import com.mytextile.dispatch.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {

    /**
     * Converts an Invoice Entity to an InvoiceResponseDto.
     */
    public InvoiceResponseDto toDto(Invoice invoice) {
        if (invoice == null) {
            return null;
        }
        
        // Gracefully handle if shipment is null (though it shouldn't be)
        Long shipmentId = (invoice.getShipment() != null) 
                            ? invoice.getShipment().getShipmentId() 
                            : null;

        return new InvoiceResponseDto(
            invoice.getInvoiceId(),
            shipmentId,
            invoice.getInvoiceNumber(),
            invoice.getAmount(),
            invoice.getStatus(),
            invoice.getIssuedDate(),
            invoice.getDueDate()
        );
    }
    
    // Note: There is no 'toEntity' method here because creating an Invoice
    // requires a 'Shipment' entity, which must be fetched by the Service layer.
    // The logic to build an Invoice is best handled directly in the 'InvoiceService'.
}