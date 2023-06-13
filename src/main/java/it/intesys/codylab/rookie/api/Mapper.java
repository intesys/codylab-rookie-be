package it.intesys.codylab.rookie.api;

public interface Mapper <ENTITY, DTO>{
    ENTITY toEntity(DTO doctorDto);

    DTO toDo(ENTITY entity);
}
