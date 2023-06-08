package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.mapper.PatientMapper;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientService {
    @Autowired
    PatientMapper mapper;
    @Autowired
    PatientRepository patientRepository;

    public PatientDTO createPatient(PatientDTO patientDTO) {
        Patient patient = mapper.toEntity(patientDTO);
        patientRepository.save(patient);
        return mapper.toDTO(patient);
    }
}
