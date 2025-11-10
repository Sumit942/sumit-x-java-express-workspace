package com.mytextile.client.mapper;

import com.mytextile.client.dto.ClientDto;
import com.mytextile.client.entity.Client;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ClientMapper {

    // Convert Client Entity -> Client DTO
    public ClientDto toDto(Client client) {
        if (Objects.isNull(client)) {
            return null;
        }

        /*ClientBankDetailsDto bankDetailsDto = null;
        if (client.getBankDetails() != null) {
            ClientBankDetails details = client.getBankDetails();
            bankDetailsDto = new ClientBankDetailsDto(
                details.getBankName(),
                details.getAccountNumber(),
                details.getIfscCode(),
                details.getBranchName()
            );
        }*/

        return new ClientDto(
            client.getClientId(),
            client.getName(),
            client.getGstin(),
            client.getContactPerson(),
            client.getEmail(),
            client.getPhone(),
            client.getBillingAddress()
//            bankDetailsDto
        );
    }

    // Convert Client DTO -> Client Entity
    public Client toEntity(ClientDto dto) {
        if (dto == null) {
            return null;
        }

        Client client = new Client();
        // Note: We don't set clientId, it's auto-generated
        client.setName(dto.name());
        client.setGstin(dto.gstin());
        client.setContactPerson(dto.contactPerson());
        client.setEmail(dto.email());
        client.setPhone(dto.phone());
        client.setBillingAddress(dto.billingAddress());

        /*if (dto.bankDetails() != null) {
            ClientBankDetails details = new ClientBankDetails();
            details.setBankName(dto.bankDetails().bankName());
            details.setAccountNumber(dto.bankDetails().accountNumber());
            details.setIfscCode(dto.bankDetails().ifscCode());
            details.setBranchName(dto.bankDetails().branchName());

            // This is the helper method we defined on the entity
            client.setBankDetails(details);
        }*/

        return client;
    }

    public void updateEntityFromDto(Client client, ClientDto dto) {
        if (dto == null || client == null) {
            return;
        }

        // Map simple fields
        client.setName(dto.name());
        client.setGstin(dto.gstin());
        client.setContactPerson(dto.contactPerson());
        client.setEmail(dto.email());
        client.setPhone(dto.phone());
        client.setBillingAddress(dto.billingAddress());

        // Handle nested Bank Details
        /*ClientBankDetails existingDetails = client.getBankDetails();
        ClientBankDetailsDto detailsDto = dto.bankDetails();

        if (detailsDto != null) {
            // Case 1: DTO has details, Entity does NOT. Create new.
            if (existingDetails == null) {
                existingDetails = new ClientBankDetails();
                client.setBankDetails(existingDetails); // Link new details
            }

            // Case 2: Update fields (works for both new and existing)
            existingDetails.setBankName(detailsDto.bankName());
            existingDetails.setAccountNumber(detailsDto.accountNumber());
            existingDetails.setIfscCode(detailsDto.ifscCode());
            existingDetails.setBranchName(detailsDto.branchName());

        } else if (existingDetails != null) {
            // Case 3: DTO has NO details, but Entity does. Remove them.
            // Setting bankDetails to null will trigger orphanRemoval
            client.setBankDetails(null);
        }*/
    }
}