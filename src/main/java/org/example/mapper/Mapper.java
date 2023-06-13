package org.example.mapper;

public interface Mapper<ENTITY,DTO> {
    ENTITY toEntity(DTO doctorDTO);

    DTO toDTO(ENTITY entity);
}

