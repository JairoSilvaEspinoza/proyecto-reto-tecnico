package com.jse.proyectoretotecnico.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum PaymentBehavior {
    CASH,
    CREDIT_CARD_DIRECT,
    CREDIT_CARD_INSTALLMENTS;

    @JsonCreator
    public static PaymentBehavior fromString(String value) {
        return Arrays.stream(PaymentBehavior.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> {
                    String allowed = Arrays.stream(PaymentBehavior.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", "));
                    return new IllegalArgumentException(
                            "Valor inválido para PaymentBehavior: " + value +
                                    ". Valores permitidos: " + allowed
                    );
                });
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}

