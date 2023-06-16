package rookie.mapper;

public interface RookieMapper<ENTITY, DTO> {
    ENTITY toEntity(DTO doctorDTO);
    DTO toDTO(ENTITY entity);
}
