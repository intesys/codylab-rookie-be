package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



    @Service
    public class DoctorService {
        @Autowired
        private DoctorRepository doctorRepository;
        @Autowired
private DoctorMapper mapper;
        public DoctorDTO createDoctor(DoctorDTO doctorDTO){

            Doctor doctor = mapper.toEntity(doctorDTO);
            doctorRepository.save(doctor);
            return mapper.toDo(doctor);
        }
    }




