package com.jse.proyectoretotecnico.service;

import com.jse.proyectoretotecnico.dto.ClientDTO;
import com.jse.proyectoretotecnico.dto.ClientUpdateDTO;
import com.jse.proyectoretotecnico.dto.ValidationRequestDTO;
import com.jse.proyectoretotecnico.dto.ValidationResponseDTO;
import com.jse.proyectoretotecnico.entity.Client;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import com.jse.proyectoretotecnico.exception.BadRequestException;
import com.jse.proyectoretotecnico.exception.ResourceNotFoundException;
import com.jse.proyectoretotecnico.mapper.ClientMapper;
import com.jse.proyectoretotecnico.proxy.PaymentBehaviorClient;
import com.jse.proyectoretotecnico.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService{

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private PaymentBehaviorClient paymentBehaviorClient;

    @Override
    public ClientDTO registerClient(ClientDTO clientDTO) {

        List<Client> clientList = clientRepository.findByUniqueCodeOrDocumentNumber(
                clientDTO.getUniqueCode(),
                clientDTO.getDocumentNumber()
        );

        clientList.stream()
                .findFirst()
                .ifPresent(client -> {
                    if (client.getUniqueCode().equals(clientDTO.getUniqueCode())) {
                        throw new RuntimeException("El código único ya existe");
                    }
                    if (client.getDocumentNumber().equals(clientDTO.getDocumentNumber())) {
                        throw new RuntimeException("El número de documento ya existe");
                    }
                });

        // Guardar cliente
        Client client = clientMapper.toEntity(clientDTO);
        client.setStatus("ACTIVE");
        Client saved = clientRepository.save(client);

        return clientMapper.toDTO(saved);
    }


    @Override
    public List<ClientDTO> listClients() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ClientDTO> findByCode(String uniqueCode) {
        return clientRepository.findByUniqueCode(uniqueCode)
                .map(clientMapper::toDTO);
    }

    @Override
    public ClientDTO updateClient(String uniqueCode, ClientUpdateDTO clientUpdateDTO) {
        Client existing = clientRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con código " + uniqueCode + " no encontrado"));

        // Validar duplicados (solo documento, no uniqueCode porque viene en path)
        if (clientRepository.existsByDocumentNumberAndIdNot(
                clientUpdateDTO.getDocumentNumber(),
                existing.getId())) {
            throw new BadRequestException("El número de documento ya está registrado en otro cliente");
        }

        // Actualizar datos
        clientMapper.updateFromUpdateDTO(clientUpdateDTO, existing);
        Client updated = clientRepository.save(existing);

        return clientMapper.toDTO(updated);
    }


    @Override
    public ClientDTO changeStatus(String uniqueCode, String status) {
        Client existing = clientRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con código " + uniqueCode + " no encontrado"));

        // Validar valores permitidos
        if (!"ACTIVE".equalsIgnoreCase(status) && !"INACTIVE".equalsIgnoreCase(status)) {
            throw new BadRequestException("El estado debe ser ACTIVE o INACTIVE");
        }

        existing.setStatus(status.toUpperCase()); // lo normalizas
        return clientMapper.toDTO(clientRepository.save(existing));
    }

    @Override
    public List<ClientDTO> listClientsByStatus(String status) {
        if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            throw new BadRequestException("El estado debe ser ACTIVE o INACTIVE");
        }

        return clientRepository.findByStatus(status).stream()
                .map(clientMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ValidationResponseDTO validatePaymentBehavior(ValidationRequestDTO requestDTO) {
        String uniqueCode = requestDTO.getUniqueCode();

        // Buscamos al cliente
        Client client = clientRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cliente con código " + uniqueCode + " no encontrado"));

        // Llamamos al proxy
        String paymentBehavior = paymentBehaviorClient.getPaymentBehavior(uniqueCode, requestDTO.getAmount());
        PaymentBehavior behaviorEnum = PaymentBehavior.valueOf(paymentBehavior);

        return new ValidationResponseDTO(
                client.getUniqueCode(),
                requestDTO.getAmount(),
                behaviorEnum
        );
    }



}
