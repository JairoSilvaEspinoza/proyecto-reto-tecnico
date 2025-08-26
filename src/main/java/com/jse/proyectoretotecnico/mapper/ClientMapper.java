package com.jse.proyectoretotecnico.mapper;

import com.jse.proyectoretotecnico.dto.ClientDTO;
import com.jse.proyectoretotecnico.dto.ClientUpdateDTO;
import com.jse.proyectoretotecnico.entity.Client;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO clientDTO);

    ClientDTO toDTO(Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @org.mapstruct.Mapping(target = "uniqueCode", ignore = true)
    void updateFromDTO(ClientDTO clientDTO, @MappingTarget Client client);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromUpdateDTO(ClientUpdateDTO clientUpdateDTO, @MappingTarget Client client);

}
