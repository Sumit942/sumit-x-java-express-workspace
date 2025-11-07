package com.mytextile.client.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/*@Getter
@Setter
@Entity
@Table(name = "client_bank_details")*/
public class ClientBankDetails {

    @Id
    private Long clientId; // This will be the Primary Key AND the Foreign Key

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "ifsc_code", nullable = false)
    private String ifscCode;

    @Column(name = "branch_name")
    private String branchName;

    // This creates the @OneToOne relationship
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // This tells JPA to use the clientId as both PK and FK
    @JoinColumn(name = "client_id")
    private Client client;
}