package com.jse.proyectoretotecnico.service;

import com.jse.proyectoretotecnico.dto.ClientDTO;
import com.jse.proyectoretotecnico.dto.ClientUpdateDTO;
import com.jse.proyectoretotecnico.dto.ValidationRequestDTO;
import com.jse.proyectoretotecnico.dto.ValidationResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    // Registrar un cliente
    ClientDTO registerClient(ClientDTO clientDTO);

    // Listar todos los clientes
    List<ClientDTO> listClients();

    // Buscar cliente por código único
    Optional<ClientDTO> findByCode(String uniqueCode);

    // Actualizar cliente
    ClientDTO updateClient(String uniqueCode, ClientUpdateDTO clientUpdateDTO);

    // Cambiar estado (activo/inactivo) de un cliente
    ClientDTO changeStatus(String uniqueCode, String status);

    // Listar clientes por estado
    List<ClientDTO> listClientsByStatus(String status);

    // Validar comportamiento de pago
    ValidationResponseDTO validatePaymentBehavior(ValidationRequestDTO requestDTO);

}
