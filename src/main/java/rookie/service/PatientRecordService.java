package rookie.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rookie.domain.PatientRecord;
import rookie.dto.PatientRecordDTO;
import rookie.exeptions.NotFound;
import rookie.mapper.PatientRecordMapper;
import rookie.repository.PatientRecordRepository;

import java.time.Instant;
import java.util.List;

@Service
public class PatientRecordService {
    @Autowired
    private PatientRecordRepository patientRecordRepository;
    @Autowired
    private PatientRecordMapper mapper;
    public PatientRecordDTO createPatientRecord(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = save(patientRecordDTO);
        return mapper.toDTO (patientRecord);
    }

    private PatientRecord save(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = mapper.toEntity (patientRecordDTO);
        Instant date = patientRecord.getDate();
        if (date == null)
            patientRecord.setDate(Instant.now());
        patientRecordRepository.save (patientRecord);
        return patientRecord;
    }

    public PatientRecordDTO getPatientRecord(Long id) throws NotFound {
        PatientRecord patientRecord = patientRecordRepository.findById (id);
        return mapper.toDTO(patientRecord);
    }

    public void updatePatientRecord(PatientRecordDTO patientRecordDTO)  throws NotFound {
        save(patientRecordDTO);
    }

    public void deletePatientRecord(Long id)  throws NotFound {
        patientRecordRepository.remove (id);
    }
}