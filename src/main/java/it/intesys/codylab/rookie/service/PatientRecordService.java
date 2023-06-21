package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.PatientRecord.PatientRecord;
import it.intesys.codylab.rookie.patientmapper.PatientRecordDTO;
import it.intesys.codylab.rookie.patientmapper.PatientRecordMapper;
import it.intesys.codylab.rookie.patientrepository.PatientRecordRepository;
import it.intesys.codylab.rookie.exception.NotFound;
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