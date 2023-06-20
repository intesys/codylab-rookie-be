package org.example.service;

import org.example.domain.Doctor;
import org.example.domain.PatientRecord;
import org.example.dto.DoctorDTO;
import org.example.dto.PatientDTO;
import org.example.dto.PatientRecordDTO;
import org.example.exceptions.NotFound;
import org.example.mapper.PatientRecordMapper;
import org.example.repository.PatientRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

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

//:: mi permettono di recuperare metodo (clausure penso)

// ctrl shift freccia posso spostare robe selezionate
