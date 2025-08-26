package com.jse.proyectoretotecnico.entity;

import com.jse.proyectoretotecnico.enums.DocumentType;
import com.jse.proyectoretotecnico.enums.PaymentBehavior;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Nuevo ID autogenerado

    @Column(length = 10, unique = true, nullable = false)
    private String uniqueCode; //  String para conservar 10 dígitos exactos

    private String firstName;

    private String lastName;

    private DocumentType documentType;

    @Column(unique = true, nullable = false)
    private String documentNumber; // soportar DNI, RUC, CE u otros

    @Column(nullable = false)
    private String status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentBehavior paymentBehavior;

}
