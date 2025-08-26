package com.jse.proyectoretotecnico.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    private String uniqueCode;
    @Min(value = 1, message = "El monto debe ser mayor que cero")
    private double amount;
}
