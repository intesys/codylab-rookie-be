package rookie.mappper;

import rookie.domain.Doctor;
import rookie.dto.DoctorDTO;

public interface Mapper<ENTITY,DTO>{
    ENTITY toEntity(DTO doctorDTO);

    DTO toDTO(ENTITY entity);
}
