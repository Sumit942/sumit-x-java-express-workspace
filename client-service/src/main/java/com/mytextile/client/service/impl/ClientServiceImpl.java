// --- File: src/main/java/com/mytextile/clientservice/service/ClientServiceImpl.java ---
package com.mytextile.client.service.impl;

import com.mytextile.client.dto.ClientDto;
import com.mytextile.client.entity.Client;
import com.mytextile.client.exception.ResourceNotFoundException;
import com.mytextile.client.mapper.ClientMapper;
import com.mytextile.client.repo.ClientRepository;
import com.mytextile.client.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor // Use constructor injection (best practice)
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper; // Helper to convert DTO <-> Entity

    @Override
    @Transactional // Ensures the whole method is one database transaction
    public ClientDto createClient(ClientDto clientDto) {
        // 1. Convert DTO to Entity
        Client client = clientMapper.toEntity(clientDto);
        
        // 2. Save the Entity
        Client savedClient = clientRepository.save(client);
        
        // 3. Convert saved Entity back to DTO and return
        return clientMapper.toDto(savedClient);
    }

    @Override
    @Transactional(readOnly = true) // readOnly is more efficient for GET
    public ClientDto getClientById(Long clientId) {
        // 1. Find the entity
        Client client = clientRepository.findById(clientId)
            .orElseThrow(() -> new ResourceNotFoundException("Client", "id", clientId));
            
        // 2. Convert to DTO and return
        return clientMapper.toDto(client);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(clientMapper::toDto) // Convert each client to a DTO
                .collect(Collectors.toList());
    }
}