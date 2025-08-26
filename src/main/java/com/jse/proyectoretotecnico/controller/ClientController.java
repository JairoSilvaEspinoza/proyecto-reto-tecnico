package com.jse.proyectoretotecnico.controller;

import com.jse.proyectoretotecnico.dto.ClientDTO;
import com.jse.proyectoretotecnico.dto.ClientUpdateDTO;
import com.jse.proyectoretotecnico.dto.ValidationRequestDTO;
import com.jse.proyectoretotecnico.dto.ValidationResponseDTO;
import com.jse.proyectoretotecnico.exception.BadRequestException;
import com.jse.proyectoretotecnico.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private void validateUniqueCode(String uniqueCode) {
        if (uniqueCode == null || !uniqueCode.matches("\\d{10}")) {
            throw new BadRequestException("El código único debe contener exactamente 10 dígitos numéricos");
        }
    }

    // Registrar cliente
    @PostMapping
    public ResponseEntity<ClientDTO> registerClient(@Valid @RequestBody ClientDTO clientDTO) {
        ClientDTO saved = clientService.registerClient(clientDTO);
        return ResponseEntity.ok(saved);
    }

    // Listar todos los clientes
    @GetMapping
    public ResponseEntity<List<ClientDTO>> listClients() {
        return ResponseEntity.ok(clientService.listClients());
    }

    // Buscar cliente por código único
    @GetMapping("/{uniqueCode}")
    public ResponseEntity<ClientDTO> findByUniqueCode(@PathVariable String uniqueCode) {
        validateUniqueCode(uniqueCode);
        return clientService.findByCode(uniqueCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Actualizar cliente por código único
    @PutMapping("/{uniqueCode}")
    public ResponseEntity<ClientDTO> updateClient(@PathVariable String uniqueCode,
                                                  @Valid @RequestBody ClientUpdateDTO clientUpdateDTO) {
        validateUniqueCode(uniqueCode);
        return ResponseEntity.ok(clientService.updateClient(uniqueCode, clientUpdateDTO));
    }

    // Cambiar estado del cliente
    @PutMapping("/{uniqueCode}/status")
    public ResponseEntity<ClientDTO> changeStatus(@PathVariable String uniqueCode,
                                                  @RequestParam String status) {
        validateUniqueCode(uniqueCode);

        // Limpiar posibles espacios
        status = status.trim();

        return ResponseEntity.ok(clientService.changeStatus(uniqueCode, status));
    }

    // Validar comportamiento de pago
    @PostMapping("/validate")
    public ResponseEntity<ValidationResponseDTO> validatePaymentBehavior(@Valid @RequestBody ValidationRequestDTO requestDTO) {

        ValidationResponseDTO response = clientService.validatePaymentBehavior(requestDTO);
        return ResponseEntity.ok(response);
    }

    // Listar clientes por estado
    @GetMapping("/status")
    public ResponseEntity<List<ClientDTO>> listClientsByStatus(@RequestParam String status) {
        return ResponseEntity.ok(clientService.listClientsByStatus(status));
    }


}
