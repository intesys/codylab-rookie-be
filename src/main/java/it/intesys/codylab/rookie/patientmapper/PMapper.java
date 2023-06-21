package it.intesys.codylab.rookie.patientmapper;

public interface PMapper<ENTITY, DTO>{
    ENTITY toEntity(DTO patientDTO);

    DTO toDTO(ENTITY patient);
}
