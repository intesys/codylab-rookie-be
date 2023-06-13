package org.example.mapper;

import org.springframework.stereotype.Component;
import org.example.dto.DoctorDTO;
import org.example.domain.Doctor;

@Component
public class DoctorMapper implements Mapper<Doctor, DoctorDTO> {
    @Override
    public Doctor toEntity(DoctorDTO dto) {
        Doctor entity = new Doctor();
        entity.setAddress(dto.getAddress());
        entity.setAvatar(dto.getAvatar());
        entity.setEmail(dto.getEmail());
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setProfession(dto.getProfession());
        entity.setSurname(dto.getSurname());
        return entity;
    }

    @Override
    public DoctorDTO toDTO(Doctor entity) {
        DoctorDTO dto = new DoctorDTO();
        dto.setAddress(entity.getAddress());
        dto.setAvatar(entity.getAvatar());
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setProfession(entity.getProfession());
        dto.setSurname(entity.getSurname());
        return dto;
    }
}
