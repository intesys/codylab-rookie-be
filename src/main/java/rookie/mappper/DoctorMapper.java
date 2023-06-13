package rookie.mappper;

import org.springframework.stereotype.Component;
import rookie.domain.Doctor;
import rookie.dto.DoctorDTO;
@Component
public class DoctorMapper implements Mapper<Doctor,DoctorDTO>{
    @Override
    public Doctor toEntity(DoctorDTO dto) {
        Doctor entity= new Doctor();
        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setAddress(dto.getAddress());
        entity.setAvatar(dto.getAvatar());
        entity.setEmail(dto.getEmail());
        entity.setId(dto.getId());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfession(dto.getProfession());
        return entity;
    }
    @Override
    public DoctorDTO toDTO(Doctor entity) {
        DoctorDTO dto= new DoctorDTO();
        dto.setName(dto.getName());
        dto.setSurname(dto.getSurname());
        dto.setAddress(dto.getAddress());
        dto.setAvatar(dto.getAvatar());
        dto.setEmail(dto.getEmail());
        dto.setId(dto.getId());
        dto.setPhoneNumber(dto.getPhoneNumber());
        dto.setProfession(dto.getProfession());
        return dto;
    }
}
