package com.jse.proyectoretotecnico.repository;

import com.jse.proyectoretotecnico.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    Optional<Client> findByUniqueCode(String uniqueCode);
    List<Client> findByStatus(String status);
    List<Client> findByUniqueCodeOrDocumentNumber(String uniqueCode, String documentNumber);

    // Para update solo validando documentNumber
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END " +
            "FROM Client c " +
            "WHERE c.documentNumber = :documentNumber " +
            "AND c.id <> :id")
    boolean existsByDocumentNumberAndIdNot(@Param("documentNumber") String documentNumber,
                                           @Param("id") Long id);



}
