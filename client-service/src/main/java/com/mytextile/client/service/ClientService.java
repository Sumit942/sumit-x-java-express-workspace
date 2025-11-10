package com.mytextile.client.service;

import com.mytextile.client.dto.ClientDto;

import java.util.List;

public interface ClientService {
    
    ClientDto createClient(ClientDto clientDto);
    
    ClientDto getClientById(Long clientId);
    
    List<ClientDto> getAllClients();

    ClientDto updateClient(Long clientId, ClientDto clientDto);

    void deleteClient(Long clientId);
}