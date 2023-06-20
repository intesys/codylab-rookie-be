package org.example.service;

import org.example.domain.Doctor;
import org.example.dto.DoctorDTO;
import org.example.dto.DoctorFilterDTO;
import org.example.dto.PatientDTO;
import org.example.exceptions.NotFound;
import org.example.mapper.DoctorMapper;
import org.example.mapper.PatientMapper;
import org.example.repository.DoctorRepository;
import org.example.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) {
        Doctor doctor = save(doctorDTO);
        return toDTO(doctor);
    }

    private Doctor save(DoctorDTO doctorDTO) {
        Doctor doctor = doctorMapper.toEntity (doctorDTO);
        doctorRepository.save (doctor);
        return doctor;
    }

    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors (pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        return doctors.stream()
                .map(this::toDTO)
                .toList();
    }

    public DoctorDTO getDoctor(Long id) throws NotFound {
        Doctor doctor = doctorRepository.findById (id);
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

    public void updateDoctor(DoctorDTO doctorDTO)  throws NotFound {
        save(doctorDTO);
    }

    public void deleteDoctor(Long id)  throws NotFound {
        doctorRepository.remove (id);
    }
}
