package com.mytextile.client.dto;

public record ClientBankDetailsDto(
    String bankName,
    String accountNumber,
    String ifscCode,
    String branchName
) {}