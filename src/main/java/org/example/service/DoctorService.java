package org.example.service;

import org.example.domain.Doctor;
import org.example.dto.DoctorDTO;
import org.example.dto.DoctorFilterDTO;
import org.example.mapper.DoctorMapper;
import org.example.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper mapper;
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = mapper.toEntity (doctorDTO);
        doctorRepository.save (doctor);
        return mapper.toDTO (doctor);
    }

    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors (pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        return doctors.stream()
                .map(mapper::toDTO)
                .toList();
    }
}
