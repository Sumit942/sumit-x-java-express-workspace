package com.mytextile.client.service;

import com.mytextile.client.dto.ClientDto;

import java.util.List;

public interface ClientService {
    
    ClientDto createClient(ClientDto clientDto);
    
    ClientDto getClientById(Long clientId);
    
    List<ClientDto> getAllClients();
    
    // You would also add methods for update and delete
}