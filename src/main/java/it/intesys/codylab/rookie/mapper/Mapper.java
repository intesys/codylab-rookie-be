package it.intesys.codylab.rookie.mapper;

public interface Mapper <ENTITY, DTO>{
    ENTITY toEntity(DTO doctorDTO);

    DTO toDTO(ENTITY entity);
}
