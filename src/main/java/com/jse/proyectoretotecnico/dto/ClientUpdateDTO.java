package com.jse.proyectoretotecnico.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jse.proyectoretotecnico.enums.DocumentType;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ClientUpdateDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    private String firstName;

    @NotBlank(message = "El apellido no puede estar vacío")
    private String lastName;

    @NotBlank(message = "El tipo de documento no puede estar vacío")
    private DocumentType documentType;

    @NotBlank(message = "El número de documento no puede estar vacío")
    private String documentNumber;

    private String status;

    @NotNull(message = "El comportamiento de pago es obligatorio")
    private PaymentBehavior paymentBehavior;
}
