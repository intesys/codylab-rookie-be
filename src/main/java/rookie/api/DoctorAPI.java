package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import rookie.dto.DoctorDTO;
import org.springframework.http.ResponseEntity;
import rookie.dto.DoctorFIlterDTO;
import rookie.exeptions.NotFound;
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

    @GetMapping("/api/doctor/{id}")
    ResponseEntity<DoctorDTO> getDoctor (@PathVariable Long id) {
        try {
            DoctorDTO dto = doctorService.getDoctor(id);
            return ResponseEntity.ok(dto);
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/doctor/{id}")
    ResponseEntity<Void> updateDoctor (@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        try {
            doctorDTO.setId(id);
            doctorService.updateDoctor (doctorDTO);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/doctor/{id}")
    ResponseEntity<Void> deleteDoctor (@PathVariable Long id) {
        try {
            doctorService.deleteDoctor (id);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Pageable pageable(Integer page, Integer size, String sort) {
        if (sort != null && !sort.isBlank()) {
            Sort.Order order;
            String[] sortSplit = sort.split(",");
            String field = sortSplit[0];
            String direction = sortSplit[1];

            if (sortSplit.length == 2) {
                order = new Sort.Order(Sort.Direction.fromString(direction), field);
            } else {
                order = Sort.Order.by(field);
            }
            return PageRequest.of(page, size, Sort.by(order));
        } else {
            return PageRequest.of(page, size);
        }
    }
}



