package com.jse.proyectoretotecnico.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum DocumentType {
    DNI,
    CE,
    PASSPORT,
    RUC;

    @JsonCreator
    public static DocumentType fromString(String value) {
        return Arrays.stream(DocumentType.values())
                .filter(e -> e.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> {
                    String allowed = Arrays.stream(DocumentType.values())
                            .map(Enum::name)
                            .collect(Collectors.joining(", "));
                    return new IllegalArgumentException(
                            "Valor inválido para DocumentType: " + value +
                                    ". Valores permitidos: " + allowed
                    );
                });
    }

    @JsonValue
    public String toValue() {
        return this.name();
    }
}
