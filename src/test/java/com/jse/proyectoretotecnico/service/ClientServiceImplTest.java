package com.jse.proyectoretotecnico.service;

import com.jse.proyectoretotecnico.dto.ClientDTO;
import com.jse.proyectoretotecnico.dto.ClientUpdateDTO;
import com.jse.proyectoretotecnico.dto.ValidationRequestDTO;
import com.jse.proyectoretotecnico.dto.ValidationResponseDTO;
import com.jse.proyectoretotecnico.entity.Client;
import com.jse.proyectoretotecnico.enums.DocumentType;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import com.jse.proyectoretotecnico.exception.BadRequestException;
import com.jse.proyectoretotecnico.exception.ResourceNotFoundException;
import com.jse.proyectoretotecnico.mapper.ClientMapper;
import com.jse.proyectoretotecnico.proxy.PaymentBehaviorClient;
import com.jse.proyectoretotecnico.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @InjectMocks
    private ClientServiceImpl clientService;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private PaymentBehaviorClient paymentBehaviorClient;

    private Client client;
    private ClientDTO clientDTO;
    private ClientUpdateDTO clientUpdateDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        client = new Client();
        client.setId(1L);
        client.setUniqueCode("1234567890");
        client.setFirstName("Jairo");
        client.setLastName("Silva");
        client.setDocumentNumber("12345678");
        client.setDocumentType(DocumentType.DNI);
        client.setStatus("ACTIVE");
        client.setPaymentBehavior(PaymentBehavior.CASH);

        clientUpdateDTO = new ClientUpdateDTO();
        clientUpdateDTO.setFirstName("Jairo");
        clientUpdateDTO.setLastName("Silva");
        clientUpdateDTO.setDocumentNumber("12345678");
        clientUpdateDTO.setDocumentType(DocumentType.DNI);
        clientUpdateDTO.setStatus("ACTIVE");
        clientUpdateDTO.setPaymentBehavior(PaymentBehavior.CASH);

        clientDTO = new ClientDTO();
        clientDTO.setUniqueCode("1234567890");
        clientDTO.setFirstName("Jairo");
        clientDTO.setLastName("Silva");
        clientDTO.setDocumentNumber("12345678");
        clientDTO.setDocumentType(DocumentType.DNI);
        clientDTO.setPaymentBehavior(PaymentBehavior.CASH);
    }

    @Test
    void testRegisterClient_Success() {
        when(clientRepository.findByUniqueCodeOrDocumentNumber(anyString(), anyString()))
                .thenReturn(Collections.emptyList());
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        ClientDTO result = clientService.registerClient(clientDTO);

        assertNotNull(result);
        assertEquals("1234567890", result.getUniqueCode());
        //verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testRegisterClient_DuplicateUniqueCode() {
        when(clientRepository.findByUniqueCodeOrDocumentNumber(anyString(), anyString()))
                .thenReturn(Collections.singletonList(client));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> clientService.registerClient(clientDTO));
        assertTrue(ex.getMessage().contains("El código único ya existe"));
    }

    @Test
    void testUpdateClient_NotFound() {
        when(clientRepository.findByUniqueCode(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> clientService.updateClient("1234567890", clientUpdateDTO));
        assertEquals("Cliente con código 1234567890 no encontrado", ex.getMessage());
    }

    @Test
    void testUpdateClient_DuplicateDocumentNumberOrUniqueCode() {
        when(clientRepository.findByUniqueCode(anyString())).thenReturn(Optional.of(client));
        when(clientRepository.existsByDocumentNumberAndIdNot(anyString(), anyLong()))
                .thenReturn(true);

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> clientService.updateClient("1234567890", clientUpdateDTO));
        assertEquals("El número de documento ya está registrado en otro cliente", ex.getMessage());
    }

    @Test
    void testChangeStatus_Success() {
        when(clientRepository.findByUniqueCode(anyString())).thenReturn(Optional.of(client));
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        ClientDTO updated = clientService.changeStatus("1234567890", "INACTIVE");

        assertEquals("1234567890", updated.getUniqueCode());
        verify(clientRepository, times(1)).save(client);
    }

    @Test
    void testChangeStatus_InvalidStatus() {
        when(clientRepository.findByUniqueCode(anyString())).thenReturn(Optional.of(client));

        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> clientService.changeStatus("1234567890", "INVALID"));
        assertEquals("El estado debe ser ACTIVE o INACTIVE", ex.getMessage());
    }

    @Test
    void testListClients() {
        when(clientRepository.findAll()).thenReturn(Collections.singletonList(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        List<ClientDTO> list = clientService.listClients();
        assertEquals(1, list.size());
    }

    @Test
    void testListClientsByStatus_Success() {
        when(clientRepository.findByStatus("ACTIVE")).thenReturn(Collections.singletonList(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        List<ClientDTO> list = clientService.listClientsByStatus("ACTIVE");
        assertEquals(1, list.size());
    }

    @Test
    void testListClientsByStatus_InvalidStatus() {
        BadRequestException ex = assertThrows(BadRequestException.class,
                () -> clientService.listClientsByStatus("INVALID"));
        assertEquals("El estado debe ser ACTIVE o INACTIVE", ex.getMessage());
    }

    @Test
    void testValidatePaymentBehavior_Success() {
        ValidationRequestDTO request = new ValidationRequestDTO();
        request.setAmount(100.0);
        request.setUniqueCode("1234567890");

        when(clientRepository.findByUniqueCode(anyString())).thenReturn(Optional.of(client));
        when(paymentBehaviorClient.getPaymentBehavior(anyString(), anyDouble()))
                .thenReturn("CASH");

        ValidationResponseDTO response = clientService.validatePaymentBehavior( request);

        assertNotNull(response);
        assertEquals(PaymentBehavior.CASH, response.getPaymentBehavior());
    }

    @Test
    void testValidatePaymentBehavior_ClientNotFound() {
        ValidationRequestDTO request = new ValidationRequestDTO();
        request.setAmount(100.0);
        request.setUniqueCode("1234567890");

        when(clientRepository.findByUniqueCode(anyString())).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> clientService.validatePaymentBehavior(request));
        assertEquals("Cliente con código 1234567890 no encontrado", ex.getMessage());
    }
}