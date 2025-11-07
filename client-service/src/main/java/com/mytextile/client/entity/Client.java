package com.mytextile.client.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long clientId;

    @Column(nullable = false)
    private String name; // The company/organization name
    
    @Column(name = "gstin", length = 15, unique = true) // Added GST number
    private String gstin;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Lob
    @Column(name = "billing_address", nullable = false)
    private String billingAddress;

    /*// This links to the new bank details entity
    @OneToOne(
        mappedBy = "client", // "client" is the field name in ClientBankDetails
        cascade = CascadeType.ALL, // If you delete a client, delete their bank info
        fetch = FetchType.LAZY,
        orphanRemoval = true
    )
    private ClientBankDetails bankDetails;*/

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /*// Helper method to link bank details
    public void setBankDetails(ClientBankDetails details) {
        if (details != null) {
            details.setClient(this);
        }
        this.bankDetails = details;
    }*/
}