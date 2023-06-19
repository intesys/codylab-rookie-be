package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rookie.dto.DoctorDTO;
import rookie.dto.DoctorFilterDTO;
import rookie.exeptions.NotFound;
import rookie.service.DoctorService;

import java.util.List;

@RestController
public class DoctorApi extends RookieAPI {

    @Autowired
    private DoctorService doctorService;

    @PostMapping ("/api/doctor/{id}")
    ResponseEntity<DoctorDTO> createDoctor (@RequestBody DoctorDTO doctorDTO) {
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping ("/api/doctor/filter")
    ResponseEntity<List<DoctorDTO>> getListDoctor (@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody DoctorFilterDTO filter) {
        Pageable pageable = pageable (pageIndex, size, sort);

        List<DoctorDTO> doctorDTOs = doctorService.getListDoctor (pageable, filter);
        return ResponseEntity.ok(doctorDTOs);
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
}