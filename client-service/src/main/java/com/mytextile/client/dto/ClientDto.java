package com.mytextile.client.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

// This DTO combines client and bank details for API requests/responses.
public record ClientDto(
    Long clientId,
    
    @NotEmpty(message = "Client name cannot be empty")
    String name,
    
    @Size(min = 15, max = 15, message = "GSTIN must be 15 characters")
    String gstin,
    
    String contactPerson,
    
    @Email(message = "Please provide a valid email")
    String email,
    
    String phone,
    String billingAddress
//    ClientBankDetailsDto bankDetails
) {}