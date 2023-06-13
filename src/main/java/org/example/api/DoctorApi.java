package org.example.api;

import org.example.dto.DoctorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DoctorApi {
    @PostMapping ("/api/doctor")
    ResponseEntity<Object> createDoctor (@RequestBody DoctorDTO doctorDTO) {

        return ResponseEntity.ok(doctorDTO);

    }
}
