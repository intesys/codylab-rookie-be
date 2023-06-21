package it.intesys.codylab.rookie.mapper;


import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.intesys.codylab.rookie.domain.BloodGroup;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.dto.BloodGroupDTO;
import it.intesys.codylab.rookie.dto.PatientDTO;

import java.util.Optional;

@Component
public class PatientMapper implements RookieMapper<Patient, PatientDTO> {
    @Autowired
    private DoctorRepository doctorRepository;

    @Override
    public Patient toEntity(PatientDTO dto) {
        Patient entity = new Patient();
        entity.setAddress(dto.getAddress());
        entity.setAvatar(dto.getAvatar());
        entity.setBloodGroup(Optional.ofNullable(dto.getBloodGroup()).map(BloodGroupDTO::name).map(BloodGroup::valueOf).orElse(null));
        entity.setChronicPatient(dto.getChronicPatient());
        entity.setEmail(dto.getEmail());
        entity.setId(dto.getId());
        entity.setIdp(dto.getIdp());
        entity.setLastAdmission(dto.getLastAdmission());
        entity.setName(dto.getName());
        entity.setNote(dto.getNote());
        entity.setOpd(dto.getOpd());
        entity.setPhoneNumber(dto.getPhoneNumber());
        entity.setSurname(dto.getSurname());
        Doctor lastDoctorVisited = Optional.ofNullable(dto.getLastDoctorVisitedId()).map(doctorRepository::findById).orElse(null);
        entity.setLastDoctorVisited(lastDoctorVisited);
        return entity;
    }

    @Override
    public PatientDTO toDTO(Patient entity) {
        PatientDTO dto = new PatientDTO();
        dto.setAddress(entity.getAddress());
        dto.setAvatar(entity.getAvatar());
        dto.setBloodGroup(Optional.ofNullable(entity.getBloodGroup()).map(BloodGroup::name).map(BloodGroupDTO::valueOf).orElse(null));
        dto.setChronicPatient(entity.getChronicPatient());
        dto.setEmail(entity.getEmail());
        dto.setId(entity.getId());
        dto.setIdp(entity.getIdp());
        dto.setLastAdmission(entity.getLastAdmission());
        dto.setName(entity.getName());
        dto.setNote(entity.getNote());
        dto.setOpd(entity.getOpd());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setSurname(entity.getSurname());
        Long lastDoctorVisitedId = Optional.ofNullable(entity.getLastDoctorVisited()).map(Doctor::getId).orElse(null);
        dto.setLastDoctorVisitedId(lastDoctorVisitedId);
        return dto;
    }
}