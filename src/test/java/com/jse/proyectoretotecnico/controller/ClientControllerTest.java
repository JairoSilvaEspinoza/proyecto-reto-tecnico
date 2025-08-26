package com.jse.proyectoretotecnico.controller;

import com.jse.proyectoretotecnico.dto.ClientDTO;
import com.jse.proyectoretotecnico.dto.ClientUpdateDTO;
import com.jse.proyectoretotecnico.dto.ValidationRequestDTO;
import com.jse.proyectoretotecnico.dto.ValidationResponseDTO;
import com.jse.proyectoretotecnico.enums.DocumentType;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import com.jse.proyectoretotecnico.exception.BadRequestException;
import com.jse.proyectoretotecnico.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class ClientControllerTest {

    @Mock
    private ClientService clientService;

    @InjectMocks
    private ClientController clientController;

    private ClientDTO clientDTO;
    private ClientUpdateDTO clientUpdateDTO;
    private ValidationRequestDTO validationRequestDTO;
    private ValidationResponseDTO validationResponseDTO;

    @BeforeEach
    void setUp() {
        // Inicializa los mocks de Mockito 4
        MockitoAnnotations.openMocks(this);

        clientDTO = new ClientDTO();
        clientDTO.setUniqueCode("1234567890");
        clientDTO.setFirstName("Jairo");
        clientDTO.setLastName("Silva");
        clientDTO.setDocumentNumber("12345678");
        clientDTO.setDocumentType(DocumentType.DNI);
        clientDTO.setStatus("ACTIVE");
        clientDTO.setPaymentBehavior(PaymentBehavior.CASH);

        clientUpdateDTO = new ClientUpdateDTO();
        clientUpdateDTO.setFirstName("Jairo");
        clientUpdateDTO.setLastName("Silva");
        clientUpdateDTO.setDocumentNumber("12345678");
        clientUpdateDTO.setDocumentType(DocumentType.DNI);
        clientUpdateDTO.setStatus("ACTIVE");

        validationRequestDTO = new ValidationRequestDTO();
        validationRequestDTO.setUniqueCode("1234567890");
        validationRequestDTO.setAmount(100.0);

        validationResponseDTO = new ValidationResponseDTO();
        validationResponseDTO.setUniqueCode("1234567890");
        validationResponseDTO.setAmount(100.0);
        validationResponseDTO.setPaymentBehavior(PaymentBehavior.CASH);
    }

    @Test
    void testRegisterClient_Success() {
        when(clientService.registerClient(any(ClientDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.registerClient(clientDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientDTO.getUniqueCode(), Objects.requireNonNull(response.getBody()).getUniqueCode());
        //De ser necesario se incluiría assertEquals para los otros campos
    }

    @Test
    void testListClients_Success() {
        when(clientService.listClients()).thenReturn(Collections.singletonList(clientDTO));

        ResponseEntity<List<ClientDTO>> response = clientController.listClients();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }

    @Test
    void testFindByUniqueCode_Success() {
        when(clientService.findByCode("1234567890")).thenReturn(Optional.of(clientDTO));

        ResponseEntity<ClientDTO> response = clientController.findByUniqueCode("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1234567890", Objects.requireNonNull(response.getBody()).getUniqueCode());
    }

    @Test
    void testFindByUniqueCode_NotFound() {
        when(clientService.findByCode("1234567890")).thenReturn(Optional.empty());

        ResponseEntity<ClientDTO> response = clientController.findByUniqueCode("1234567890");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindByUniqueCode_InvalidCode() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> clientController.findByUniqueCode("invalid"));

        assertEquals("El código único debe contener exactamente 10 dígitos numéricos", ex.getMessage());
    }

    @Test
    void testUpdateClient_Success() {
        when(clientService.updateClient(eq("1234567890"), any(ClientUpdateDTO.class))).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.updateClient("1234567890", clientUpdateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1234567890", Objects.requireNonNull(response.getBody()).getUniqueCode());
        //De ser necesario se incluiría assertEquals para los otros campos
    }

    @Test
    void testChangeStatus_Success() {
        clientDTO.setStatus("INACTIVE");
        when(clientService.changeStatus("1234567890", "INACTIVE")).thenReturn(clientDTO);

        ResponseEntity<ClientDTO> response = clientController.changeStatus("1234567890", "INACTIVE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("INACTIVE", Objects.requireNonNull(response.getBody()).getStatus());
    }

    @Test
    void testValidatePaymentBehavior_Success() {
        when(clientService.validatePaymentBehavior(any(ValidationRequestDTO.class)))
                .thenReturn(validationResponseDTO);

        ResponseEntity<ValidationResponseDTO> response = clientController.validatePaymentBehavior(validationRequestDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("1234567890", Objects.requireNonNull(response.getBody()).getUniqueCode());
        assertEquals(100.0, response.getBody().getAmount());
    }

    @Test
    void testListClientsByStatus_Success() {
        when(clientService.listClientsByStatus("ACTIVE")).thenReturn(Collections.singletonList(clientDTO));

        ResponseEntity<List<ClientDTO>> response = clientController.listClientsByStatus("ACTIVE");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    }
}
