package it.intesys.codylab.rookie.mapper;

import it.intesys.codylab.rookie.domain.PatientRecord;
import it.intesys.codylab.rookie.dto.PatientRecordDTO;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PatientRecordMapper implements Mapper<PatientRecordDTO, PatientRecord> {
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorMapper doctorMapper;

    @Override
    public PatientRecord toEntity(PatientRecordDTO dto) {
        PatientRecord patientRecord = new PatientRecord();

        patientRecord.setId(dto.getId());
        patientRecord.setDate(dto.getDate());
        if (dto.getDoctor() != null)
            patientRecord.setDoctor(doctorMapper.toEntity(dto.getDoctor()));
        if (dto.getPatientId() != null)
            patientRecord.setPatient(patientRepository.findById(dto.getPatientId()));
        patientRecord.setReasonVisit(dto.getReasonVisit());
        patientRecord.setTreatmentMade(dto.getTreatmentMade());
        patientRecord.setTypeVisit(dto.getTypeVisit());

        return patientRecord;
    }

    @Override
    public PatientRecordDTO toDTO(PatientRecord entity) {
        PatientRecordDTO dto = new PatientRecordDTO();

        dto.setId(entity.getId());
        dto.setDate(entity.getDate());
        if (entity.getDoctor() != null)
            dto.setDoctor(doctorMapper.toDTO(entity.getDoctor()));
        if (entity.getPatient() != null)
            dto.setPatientId(entity.getPatient().getId());
        dto.setReasonVisit(entity.getReasonVisit());
        dto.setTreatmentMade(entity.getTreatmentMade());
        dto.setTypeVisit(entity.getTypeVisit());

        return dto;
    }
}
