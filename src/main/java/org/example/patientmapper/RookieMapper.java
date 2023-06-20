package org.example.patientmapper;

import org.example.PatientRecord.PatientRecord;

public interface  RookieMapper<Entity,DTO> {
    Entity toEntity(DTO doctorDto);

    DTO toDTO(Entity entity);

}
