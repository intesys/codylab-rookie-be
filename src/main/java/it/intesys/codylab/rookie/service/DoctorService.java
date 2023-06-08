package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.mapper.DoctorMapper;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class DoctorService {
    @Autowired
    DoctorMapper mapper;
    @Autowired
    DoctorRepository doctorRepository;
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = mapper.toEntity(doctorDTO);
        doctorRepository.save(doctor);
        return mapper.toDTO(doctor);
    }

    public Page<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        return doctors.map(this::toDTO);
    }

    private DoctorDTO toDTO(Doctor doctor) {
        DoctorDTO dto = mapper.toDTO(doctor);
        return dto;
    }
}
