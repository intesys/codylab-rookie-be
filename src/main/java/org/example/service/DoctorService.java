package org.example.service;

import org.example.domain.Doctor;
import org.example.dto.DoctorDTO;
import org.example.dto.DoctorFilterDTO;
import org.example.exceptions.NotFound;
import org.example.mapper.DoctorMapper;
import org.example.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
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
        Doctor doctor = save(doctorDTO);
        return mapper.toDTO (doctor);
    }

    private Doctor save(DoctorDTO doctorDTO) {
        Doctor doctor = mapper.toEntity (doctorDTO);
        doctorRepository.save (doctor);
        return doctor;
    }

    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors (pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        return doctors.stream()
                .map(mapper::toDTO)
                .toList();
    }

    public DoctorDTO getDoctor(Long id) throws NotFound {
        Doctor doctor = doctorRepository.findById (id);
        return mapper.toDTO(doctor);
    }

    public void updateDoctor(DoctorDTO doctorDTO)  throws NotFound {
        save(doctorDTO);
    }

    public void deleteDoctor(Long id)  throws NotFound {
        doctorRepository.remove (id);
    }
}

//:: mi permettono di recuperare metodo (clausure penso)

// ctrl shift freccia posso spostare robe selezionate
