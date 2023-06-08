package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper implements Mapper<DoctorDTO, Doctor> {

    @Override
    public Doctor toEntity(DoctorDTO dto) {
        if (dto == null)
            return null;

        Doctor doctor = new Doctor();
        doctor.setId(dto.getId());
        doctor.setAddress(dto.getAddress());
        doctor.setAvatar(dto.getAvatar());
        doctor.setEmail(dto.getEmail());
        doctor.setName(dto.getName());
        doctor.setPhoneNumber(dto.getPhoneNumber());
        doctor.setSurname(dto.getSurname());
        doctor.setProfession(dto.getProfession());
        return doctor;
    }

    @Override
    public DoctorDTO toDTO(Doctor entity) {
        if (entity == null)
            return null;

        DoctorDTO dto = new DoctorDTO();
        dto.setId(entity.getId());
        dto.setAddress(entity.getAddress());
        dto.setAvatar(entity.getAvatar());
        dto.setEmail(entity.getEmail());
        dto.setName(entity.getName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setSurname(entity.getSurname());
        dto.setProfession(entity.getProfession());
        return dto;
    }
}
