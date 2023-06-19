package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.domain.PatientRecord;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.PatientRecordDTO;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PatientRecordMapper implements RookieMapper<PatientRecord, PatientRecordDTO> {

    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorMapper doctorMapper;

    @Override
    public PatientRecord toEntity(PatientRecordDTO dto) {
        PatientRecord entity = new PatientRecord();
        entity.setId(dto.getId());
        entity.setDate(dto.getDate());
        entity.setDoctor(Optional.ofNullable(dto.getDoctor())
                .map(DoctorDTO::getID)
                .map(doctorRepository::findById)
                .orElse(null));
        entity.setPatient(Optional.ofNullable(dto.getPatientId())
                .map(patientRepository::findById)
                .orElse(null));
        entity.setReasonVisit(dto.getReasonVisit());
        entity.setTreatmentMade(dto.getTreatmentMade());
        entity.setTypeVisit(dto.getTypeVisit());
        return entity;
    }

    @Override
    public PatientRecordDTO toDTO(PatientRecord entity) {
        PatientRecordDTO dto = new PatientRecordDTO();
        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        dto.setDoctor(Optional.ofNullable(entity.getDoctor())
                .map(doctorMapper::toDTO)
                .orElse(null));
        dto.setPatientId(Optional.ofNullable(entity.getPatient())
                .map(Patient::getId)
                .orElse(null));
        dto.setReasonVisit(entity.getReasonVisit());
        dto.setTreatmentMade(entity.getTreatmentMade());
        dto.setTypeVisit(entity.getTypeVisit());
        return dto;
    }
}
