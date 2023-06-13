package org.example;

public interface Mapper <ENTITY, DTO>{
    ENTITY toEntity(DTO doctorDto);

    DTO toDo(ENTITY entity);
}
