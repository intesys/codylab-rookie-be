package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.PatientRecord;
import it.intesys.codylab.rookie.dto.PatientRecordDTO;
import it.intesys.codylab.rookie.mapper.PatientRecordMapper;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import it.intesys.codylab.rookie.repository.PatientRecordRepository;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@Transactional
public class PatientRecordService {
    @Autowired
    PatientRecordMapper mapper;
    @Autowired
    PatientRecordRepository patientRecordRepository;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    public PatientRecordDTO createPatientRecord(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = mapper.toEntity(patientRecordDTO);

        if (patientRecord.getDate() == null)
            patientRecord.setDate(Instant.now());
        if (patientRecord.getPatient() == null)
            throw new IllegalArgumentException("No patient");
        if (patientRecord.getDoctor() == null)
            throw new IllegalArgumentException("No doctor");

        patientRecordRepository.save(patientRecord);
        return mapper.toDTO(patientRecord);
    }
}
