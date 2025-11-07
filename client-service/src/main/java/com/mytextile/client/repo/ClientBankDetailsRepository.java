package com.mytextile.client.repo;

import com.mytextile.client.entity.ClientBankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientBankDetailsRepository extends JpaRepository<ClientBankDetails, Long> {
    // We may not even need this if we only access bank details via the Client entity,
    // but it's good practice to create it.
}