package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.exceptions.NotFound;
import it.intesys.codylab.rookie.mapper.DoctorMapper;
import it.intesys.codylab.rookie.mapper.Mapper;
import it.intesys.codylab.rookie.mapper.PatientMapper;
import it.intesys.codylab.rookie.repository.DoctorRepository;
import it.intesys.codylab.rookie.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.Not;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private PatientMapper patientMapper;
    @Autowired
    private PatientRepository patientRepository;

    public Doctor save(DoctorDTO doctorDTO) throws NotFound {
        Doctor doctor =  doctorMapper.toEntity(doctorDTO);
        doctorRepository.save (doctor);
        return doctor;

    }
    public DoctorDTO createDoctor(DoctorDTO doctorDTO) throws NotFound {
        Doctor doctor = save(doctorDTO);
        return  toDTO(doctor);
    }

    private Doctor saveDoctor(DoctorDTO doctorDTO) throws NotFound {
        Doctor doctor = doctorMapper.toEntity(doctorDTO);
        doctorRepository.save(doctor);
        return  doctor;
    }

    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors(pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        //List <DoctorDTO> dtos= doctors.stream().map(mapper::toDTO).toList();

        //recupero riferimento a un metodo ::
        return  doctors.stream().map(this::toDTO).toList();
    }

    public DoctorDTO getDoctor(Long id) throws NotFound {
        Doctor doctor =  doctorRepository.findById(id);
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

    public void deleteDoctor(Long id) throws  NotFound{
        doctorRepository.remove(id);
    }

    public void updateDoctor(DoctorDTO doctorDTO) throws NotFound {
        save(doctorDTO);
    }
}
