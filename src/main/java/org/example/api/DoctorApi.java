package org.example.api;

import org.example.dto.DoctorDTO;
import org.example.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DoctorApi {
    @Autowired
    private DoctorService doctorService;
    @PostMapping ("/api/doctor")
    ResponseEntity<Object> createDoctor (@RequestBody DoctorDTO doctorDTO) {
        DoctorDTO doctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctor);
    }
}
