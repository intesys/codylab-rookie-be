package org.example.service;


import org.example.domain.Patient;
import org.example.patientdto.PatientDTO;
import org.example.patientdto.PatientFilterDTO;
import org.example.patientmapper.PatientMapper;
import org.example.patientmapper.PatientRecordMapper;
import org.example.patientrepository.PatientRecordRepository;
import org.example.patientrepository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.example.exception.NotFound;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientRecordRepository patientRecordRepository;
    @Autowired
    private PatientMapper mapper;
    @Autowired
    private PatientRecordMapper patientRecordMapper;
    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = save(patientDTO);
        return mapper.toDTO (patient);
    }

    private Patient save(PatientDTO patientDTO) {
        Patient patient = mapper.toEntity (patientDTO);
        patientRepository.save (patient);
        return patient;
    }

    public List<PatientDTO> getListPatient(Pageable pageable, PatientFilterDTO filter) {
        List<Patient> patients = patientRepository.getPatients (pageable, filter.getText(), filter.getId(), filter.getOpd(), filter.getIdp(), filter.getDoctorId());
        return patients.stream()
                .map(this::toDTO)
                .toList();
    }

    private PatientDTO toDTO(Patient patient) {
        PatientDTO patientDTO = mapper.toDTO(patient);
        patientDTO.setPatientRecords(patientRecordRepository.findLatestRecordByPatient (patient)
                .stream()
                .map(patientRecordMapper::toDTO)
                .toList());
        return patientDTO;
    }

    public PatientDTO getPatient(Long id) throws NotFound {
        Patient patient = patientRepository.findById (id);
        PatientDTO patientDTO = mapper.toDTO(patient);
        patientDTO.setPatientRecords(patientRecordRepository.findByPatient (patient)
                .stream()
                .map(patientRecordMapper::toDTO)
                .toList());
        return patientDTO;
    }

    public void updatePatient(PatientDTO patientDTO)  throws NotFound {
        save(patientDTO);
    }

    public void deletePatient(Long id)  throws NotFound {
        patientRepository.remove (id);
    }
}