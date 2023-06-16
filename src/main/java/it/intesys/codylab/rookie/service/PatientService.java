package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Patient;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.dto.PatientFilterDTO;
import it.intesys.codylab.rookie.exceptions.NotFound;
import it.intesys.codylab.rookie.mapper.PatientMapper;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PatientMapper mapper;

    public Patient save(PatientDTO patientDTO) throws NotFound {
        Patient patient =  mapper.toEntity(patientDTO);
        patientRepository.save (patient);
        return patient;

    }
    public PatientDTO createPatient(PatientDTO patientDTO) throws NotFound {
        Patient patient = save(patientDTO);
        return  mapper.toDTO(patient);
    }

    private Patient savePatient(PatientDTO patientDTO) throws NotFound {
        Patient patient = mapper.toEntity(patientDTO);
        patientRepository.save(patient);
        return  patient;
    }

    public List<PatientDTO> getListPatient(Pageable pageable, PatientFilterDTO filter) {
        List<Patient> patients = patientRepository.getPatients(pageable, filter.getText(), filter.getId(), filter.getOpd(), filter.getIdp(), filter.getDoctorId());
        //List <PatientDTO> dtos= patients.stream().map(mapper::toDTO).toList();

        //recupero riferimento a un metodo ::
        return  patients.stream().map(mapper::toDTO).toList();
    }

    public PatientDTO getPatient(Long id) throws NotFound {
        Patient patient =  patientRepository.findById(id);
        return mapper.toDTO(patient);
    }

    public void deletePatient(Long id) throws  NotFound{
        patientRepository.remove(id);
    }

    public void updatePatient(PatientDTO patientDTO) throws NotFound {
        save(patientDTO);
    }
}
