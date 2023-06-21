package it.intesys.codylab.rookie.patientmapper;

public interface  RookieMapper<Entity,DTO> {
    Entity toEntity(DTO doctorDto);

    DTO toDTO(Entity entity);

}
