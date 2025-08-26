package com.jse.proyectoretotecnico.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jse.proyectoretotecnico.enums.DocumentType;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientDTO {

    private Long id; // ID generado por la base de datos, opcional al registrar


    @NotBlank(message = "El código único no puede estar vacío")
    @Size(min = 10, max = 10, message = "El código único debe tener exactamente 10 dígitos")
    @Pattern(regexp = "\\d{10}", message = "El código único solo puede contener números")
    private String uniqueCode;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @NotNull(message = "El tipo de documento no puede estar vacío")
    private DocumentType documentType;

    @NotBlank(message = "El número de documento no puede estar vacío")
    @Size(min = 6, max = 12, message = "El número de documento debe tener entre 6 y 12 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "El número de documento solo puede contener letras y números")
    private String documentNumber;

    private String status;

    @NotNull(message = "El comportamiento de pago es obligatorio")
    private PaymentBehavior paymentBehavior;

}
