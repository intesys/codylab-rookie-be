package it.intesys.codylab.rookie.service;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.mapper.DoctorMapper;
import it.intesys.codylab.rookie.repository.DoctorRepository;
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

    public DoctorDTO createDoctor(DoctorDTO doctorDTO){
        Doctor doctor =  mapper.toEntity(doctorDTO);
        doctorRepository.saveDoctor(doctor);
        return mapper.toDTO(doctor);

    }

    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFilterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors(pageable, filter.getName(), filter.getSurname(), filter.getProfession());
        //List <DoctorDTO> dtos= doctors.stream().map(mapper::toDTO).toList();

        //recupero riferimento a un metodo ::
        return  doctors.stream().map(mapper::toDTO).toList();
    }
}
