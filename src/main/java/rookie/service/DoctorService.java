package rookie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rookie.domain.Doctor;
import rookie.dto.DoctorDTO;
import rookie.mappper.DoctorMapper;
import rookie.repository.DoctorRepository;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper mapper;
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = mapper.toEntity(doctorDTO);
        doctorRepository.save(doctor);
        return mapper.toDTO(doctor);
    }
}
