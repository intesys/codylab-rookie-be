package it.intesys.codylab.rookie.api;


import it.intesys.codylab.rookie.dto.DoctorDTO;
import org.springframework.stereotype.Component;

@Component

public class DoctorMapper implements Mapper<Doctor,DoctorDTO> {

        @Override
        public Doctor toEntity(DoctorDTO dto){
            Doctor entity = new Doctor();
            entity.setAddress(dto.getAddress());
            entity.setName(dto.getName());
            entity.setSurname(dto.getSurname());
            entity.setAvatar(dto.getAvatar());
            entity.setEmail(dto.getEmail());
            entity.setProfession(dto.getProfession());
            return entity;


    }

    @Override
    public DoctorDTO toDo(Doctor entity) {
        DoctorDTO dto = new DoctorDTO();
        dto.setAddress(dto.getAddress());
        dto.setName(dto.getName());
        dto.setSurname(dto.getSurname());
        dto.setAvatar(dto.getAvatar());
        dto.setEmail(dto.getEmail());
        dto.setProfession(dto.getProfession());
        return dto;
    }



}
