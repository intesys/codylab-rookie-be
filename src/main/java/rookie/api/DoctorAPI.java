package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestParam;
import rookie.dto.DoctorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rookie.service.DoctorService;
import rookie.dto.DoctorFilterDTO;

import java.util.List;

@RestController
public class DoctorAPI {
    @Autowired
    private DoctorService doctorService;
    @PostMapping("/api/doctor")
    ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO){
        DoctorDTO doctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping ("/api/doctor/filter")
     ResponseEntity<List<DoctorDTO>> getListDoctor(@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody DoctorFilterDTO filter){
        Pageable pageable = pageable(pageIndex, size, sort);
        List<DoctorDTO> doctorDTOs= doctorService.getListDoctor(pageable, filter);
        return ResponseEntity.ok(doctorDTOs);
    }

    private Pageable pageable(Integer pageIndex, Integer size, String sort) {
        return PageRequest.of(pageIndex, size, Sort.unsorted());
    }
}
