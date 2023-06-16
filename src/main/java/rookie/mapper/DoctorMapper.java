package rookie.mapper;

import org.springframework.stereotype.Component;
import rookie.domain.Doctor;
import rookie.dto.DoctorDTO;

@Component
public class DoctorMapper implements RookieMapper<Doctor, DoctorDTO> {
    @Override
    public Doctor toEntity(DoctorDTO dto){
        Doctor entity = new Doctor();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setAddress(dto.getAddress());
        entity.setEmail(dto.getEmail());
        entity.setAvatar(dto.getAvatar());
        entity.setProfession(dto.getProfession());
        return entity;
    }
    @Override
    public DoctorDTO toDTO(Doctor entity){
        DoctorDTO dto = new DoctorDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setAddress(entity.getAddress());
        dto.setEmail(entity.getEmail());
        dto.setAvatar(entity.getAvatar());
        dto.setProfession(entity.getProfession());
        return dto;
    }
}
