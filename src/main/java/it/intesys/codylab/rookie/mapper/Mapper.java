package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;

public interface Mapper<ENTITY,DTO> {
    ENTITY toEntity(DTO doctorDTO);

    DTO toDTO(ENTITY entity);
}
