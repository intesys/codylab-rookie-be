package rookie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rookie.domain.Patient;
import rookie.domain.Doctor;
import rookie.domain.PatientRecord;
import rookie.dto.PatientDTO;
import rookie.dto.PatientFilterDTO;
import rookie.exceptions.NotFound;
import rookie.mapper.PatientMapper;
import rookie.mapper.PatientRecordMapper;
import rookie.repository.DoctorRepository;
import rookie.repository.PatientRecordRepository;
import rookie.repository.PatientRepository;

import java.util.List;
import java.util.function.Function;
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

    public PatientDTO createPatient(PatientDTO patientDTO){
        Patient patient = save(patientDTO);
        return mapper.toDTO (patient);
    }

    public List<PatientDTO> getListPatient(Pageable pageable, PatientFilterDTO filter) {
        List<Patient> patients = patientRepository.getPatients (pageable, filter.getText(), filter.getId(), filter.getOpd(), filter.getIdp(), filter.getDoctorId());
        return patients.stream().map(patient->this.toDTO(patient, false)).toList();
    }

    private Patient save(PatientDTO patientDTO) {
        Patient patient = mapper.toEntity (patientDTO);
        patientRepository.save(patient);
        return patient;
    }

    public PatientDTO getPatient(Long id) throws NotFound {
        Patient patient = patientRepository.findById(id);
        return toDTO(patient,true);
    }

    public void updatePatient(PatientDTO patientDTO) throws NotFound {
        save(patientDTO);
    }

    public void deletePatient(Long id) throws NotFound {
        patientRepository.remove(id);
    }
    private PatientDTO toDTO(Patient patient, boolean allRecord) {
        PatientDTO patientDTO = mapper.toDTO(patient);

        Function<Patient, List<PatientRecord>> recordProvider =
                allRecord? patientRecordRepository::findByPatient: patientRecordRepository::findLatestRecordByPatient;

        patientDTO.setPatientRecords(recordProvider.apply(patient)
                .stream()
                .map(patientRecordMapper::toDTO)
                .toList());

        patientDTO.setDoctorIds(doctorRepository.findByPatient(patient)
                .stream()
                .map(Doctor::getId)
                .toList());

        return patientDTO;
    }
}
