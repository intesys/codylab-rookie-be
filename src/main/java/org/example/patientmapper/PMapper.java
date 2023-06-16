package org.example.patientmapper;

public interface PMapper<ENTITY, DTO>{
    ENTITY toEntity(DTO patientDTO);

    DTO toDTO(ENTITY patient);
}
