package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.domain.PatientDoctor;
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
import java.util.List;
import java.util.Optional;

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
        save(patientRecord);
        return mapper.toDTO(patientRecord);
    }
    public void updatePatientRecord(PatientRecordDTO patientRecordDTO) {
        PatientRecord patientRecord = mapper.toEntity(patientRecordDTO);
        save(patientRecord);
    }

    private void save(PatientRecord patientRecord) {
        if (patientRecord.getDate() == null)
            patientRecord.setDate(Instant.now());
        if (patientRecord.getPatient() == null)
            throw new IllegalArgumentException("No patient");
        if (patientRecord.getDoctor() == null)
            throw new IllegalArgumentException("No doctor");

        associatePatientDoctor(patientRecord);

        patientRecordRepository.save(patientRecord);
    }

    private void associatePatientDoctor(PatientRecord patientRecord) {
        Doctor doctor = patientRecord.getDoctor();
        Patient patient = patientRecord.getPatient();
        patient.setDoctors(List.of(new PatientDoctor(patient, doctor, Instant.now())));
        patientRepository.save(patient);
        patientRecord.setDoctor(doctorRepository.findById(patientRecord.getDoctor().getId()));
    }

    public PatientRecordDTO getPatientRecord(Long id) {
        PatientRecord patientRecord = patientRecordRepository.findById(id);
        return Optional.ofNullable(patientRecord).map(mapper::toDTO).orElse(null);
    }

    public void deletePatientRecord(Long id) {
        patientRecordRepository.deleteById(id);
    }
}
