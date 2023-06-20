package org.example.service;

import org.example.domain.Doctor;
import org.example.domain.Patient;
import org.example.domain.PatientRecord;
import org.example.dto.PatientDTO;
import org.example.dto.PatientFilterDTO;
import org.example.dto.PatientRecordDTO;
import org.example.exceptions.NotFound;
import org.example.mapper.PatientMapper;
import org.example.mapper.PatientRecordMapper;
import org.example.repository.DoctorRepository;
import org.example.repository.PatientRecordRepository;
import org.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    @Autowired
    private DoctorRepository doctorRepository;

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

        patientDTO.setDoctorIds(doctorRepository.findByPatient(patient)
                .stream()
                .map(Doctor::getId)
                .toList());

        return patientDTO;
    }

    public PatientDTO getPatient(Long id) throws NotFound {
        Patient patient = patientRepository.findById (id);
        return toDTO(patient);
    }

    public void updatePatient(PatientDTO patientDTO)  throws NotFound {
        save(patientDTO);
    }

    public void deletePatient(Long id)  throws NotFound {
        patientRepository.remove (id);
    }
}