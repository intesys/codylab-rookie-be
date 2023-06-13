package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.mapper.DoctorMapper;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper mapper;

    public DoctorDTO createDoctor(DoctorDTO doctorDTO){
        Doctor doctor =  mapper.toEntity(doctorDTO);
        doctorRepository.saveDoctor(doctor);
        return mapper.toDTO(doctor);

    }
}
