package com.mytextile.client.mapper;

import com.mytextile.client.dto.ClientBankDetailsDto;
import com.mytextile.client.dto.ClientDto;
import com.mytextile.client.entity.Client;
import com.mytextile.client.entity.ClientBankDetails;
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
}