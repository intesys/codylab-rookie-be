package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import org.example.service.DoctorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DoctorApi {
    private DoctorService doctorService;
    @PostMapping ("/api/doctor")
    ResponseEntity<DoctorDTO> createDoctor (@RequestBody DoctorDTO doctorDTO) {
        return ResponseEntity.ok(doctorDTO);
    }
}
