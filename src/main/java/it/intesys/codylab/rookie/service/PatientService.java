package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.domain.PatientRecord;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.dto.PatientFilterDTO;
import it.intesys.codylab.rookie.mapper.PatientMapper;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import it.intesys.codylab.rookie.repository.PatientRecordRepository;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PatientService {
    @Autowired
    PatientMapper mapper;
    @Autowired
    PatientRepository patientRepository;
    @Autowired
    DoctorRepository doctorRepository;
    @Autowired
    PatientRecordRepository patientRecordRepository;

    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = mapper.toEntity(patientDTO);
        patientRepository.save(patient);
        return mapper.toDTO(patient);
    }


    public Page<PatientDTO> getListPatient(Pageable pageable, PatientFilterDTO filter) {
        String text = filter.getText();
        Long id = filter.getId();
        Long opd = filter.getOpd();
        Long idp = filter.getIdp();
        Doctor doctor = Optional.ofNullable(filter.getDoctorId())
                .map(doctorRepository::findById)
                .orElse(null);
        Page<Patient> patients = patientRepository.findAll(pageable, text, id, opd, idp, doctor);
        patients.forEach (this::setLatestRecords);
        return patients.map(this::toDTO);
    }

    private void setLatestRecords(Patient patient) {
        patient.setPatientRecords(patientRecordRepository.findLatestByPatient(patient, 5));
    }

    private PatientDTO toDTO(Patient patient) {
        return mapper.toDTO(patient);
    }

    public void updatePatient(PatientDTO patientDTO) {
        Patient patient = mapper.toEntity(patientDTO);
        patientRepository.save(patient);
    }

    public PatientDTO getPatient(Long id) {
        Patient patient = patientRepository.findById(id);
        List<PatientRecord> patientRecords = patientRecordRepository.findByPatient(patient);
        patient.setPatientRecords(patientRecords);
        return toDTO(patient);
    }

    public void deletePatient(Long id) {
        patientRepository.deleteById(id);
    }
}
