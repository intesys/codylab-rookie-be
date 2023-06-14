package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import rookie.dto.DoctorDTO;
import org.springframework.http.ResponseEntity;
import rookie.dto.DoctorFIlterDTO;
import rookie.service.DoctorService;

import java.util.List;

@RestController
public class DoctorAPI {
    @Autowired
    private DoctorService doctorService;
    @PostMapping("/api/doctor")
     ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO){
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }
    @PostMapping("/api/doctor/filter")
    ResponseEntity<List<DoctorDTO>> getListDoctor(@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam("sort") String sort, @RequestBody DoctorFIlterDTO filter){
        Pageable pageable=pageable(pageIndex,size,sort);
        List<DoctorDTO> doctorDTOS = doctorService.getListDoctor(pageable, filter);
        return ResponseEntity.ok(doctorDTOS);
    }

    private Pageable pageable(Integer pageIndex, Integer size, String sort) {
        return PageRequest.of(pageIndex,size, Sort.unsorted());
    }
}
