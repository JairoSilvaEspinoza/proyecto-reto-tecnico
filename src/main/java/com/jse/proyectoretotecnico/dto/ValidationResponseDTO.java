package com.jse.proyectoretotecnico.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationResponseDTO {

    private String uniqueCode;
    private double amount;
    private PaymentBehavior paymentBehavior;

}
