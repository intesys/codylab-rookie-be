package rookie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rookie.domain.Doctor;
import rookie.dto.DoctorDTO;
import rookie.dto.DoctorFilterDTO;
import rookie.dto.PatientDTO;
import rookie.exceptions.NotFound;
import rookie.mapper.DoctorMapper;
import rookie.mapper.PatientMapper;
import rookie.repository.DoctorRepository;
import rookie.repository.PatientRepository;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private PatientMapper patientMapper;

    public DoctorDTO createDoctor(DoctorDTO doctorDTO){
        Doctor doctor = save(doctorDTO);
        return toDTO(doctor);
    }

    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors(pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        return doctors.stream().map(this::toDTO).toList();
    }

    private Doctor save(DoctorDTO doctorDTO) {
        Doctor doctor = doctorMapper.toEntity (doctorDTO);
        doctorRepository.save(doctor);
        return doctor;
    }

    public DoctorDTO getDoctor(Long id) throws NotFound {
        Doctor doctor = doctorRepository.findById(id);
        return toDTO(doctor);
    }
    private DoctorDTO toDTO(Doctor doctor) {
        DoctorDTO dto = doctorMapper.toDTO(doctor);
        List<PatientDTO> latestPatientDTOs = patientRepository.findLatestPatientsByDoctor(doctor)
                .stream()
                .map(patientMapper::toDTO)
                .toList();
        dto.setLatestPatients(latestPatientDTOs);
        return dto;
    }

    public void updateDoctor(DoctorDTO doctorDTO) throws NotFound {
        save(doctorDTO);
    }

    public void deleteDoctor(Long id) throws NotFound {
        doctorRepository.remove(id);
    }
}
