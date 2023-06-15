package rookie.service;

import com.sun.source.doctree.DocCommentTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import rookie.domain.Doctor;
import rookie.dto.DoctorDTO;
import rookie.dto.DoctorFIlterDTO;
import rookie.exeptions.NotFound;
import rookie.mapper.DoctorMapper;
import rookie.repository.DoctorRepository;

import java.util.List;

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
    private Doctor save(DoctorDTO doctorDTO) {
        Doctor doctor = mapper.toEntity (doctorDTO);
        doctorRepository.save (doctor);
        return doctor;
    }
    public List<DoctorDTO> getListDoctor(Pageable pageable, DoctorFIlterDTO filter) {
        List<Doctor> doctors = doctorRepository.getDoctors(pageable,filter.getName(),filter.getSurname(), filter.getProfession());
        return doctors.stream().map(mapper::toDTO).toList();
    }

    public DoctorDTO getDoctor(Long id) {
        Doctor doctor= doctorRepository.findById(id);
        return mapper.toDTO(doctor);
    }

    public void updateDoctor(DoctorDTO doctorDTO)  throws NotFound {
        save(doctorDTO);
    }

    public void deleteDoctor(Long id)  throws NotFound {
        doctorRepository.remove(id);
    }
}
