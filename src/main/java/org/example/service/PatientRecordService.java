package org.example.service;

import org.example.PatientRecord.PatientRecord;
import org.example.exception.NotFound;
import org.example.patientmapper.PatientRecordDTO;
import org.example.patientmapper.PatientRecordMapper;
import org.example.patientrepository.PatientRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class PatientRecordService {
    @Autowired
    private PatientRecordRepository patientRecordRepository;
    @Autowired
    private PatientRecordMapper mapper;

    public PatientRecordDTO createPatientRecord(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = save(patientRecordDTO);
        return mapper.toDTO(patientRecord);
    }

    private PatientRecord save(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = mapper.toEntity(patientRecordDTO);
        Instant date = patientRecord.getDate();
        if (date == null)
            patientRecord.setDate(Instant.now());
        patientRecordRepository.save(patientRecord);
        return patientRecord;
    }

    public PatientRecordDTO getPatientRecord(Long id) throws NotFound {
        PatientRecord patientRecord = patientRecordRepository.findById(id);
        return mapper.toDTO(patientRecord);
    }

    public void updatePatientRecord(PatientRecordDTO patientRecordDTO) throws NotFound {
        save(patientRecordDTO);
    }

    public void deletePatientRecord(Long id) throws NotFound {
        patientRecordRepository.remove(id);
    }
}