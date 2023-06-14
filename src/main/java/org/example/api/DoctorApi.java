package org.example.api;

import org.example.dto.DoctorDTO;
import org.example.dto.DoctorFilterDTO;
import org.example.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DoctorApi {
    @Autowired
    private DoctorService doctorService;

    @PostMapping ("/api/doctor")
    ResponseEntity<DoctorDTO> createDoctor (@RequestBody DoctorDTO doctorDTO){
        DoctorDTO doctor = doctorService.createDoctor(doctorDTO);
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping ("/api/doctor/filter")
    ResponseEntity<List<DoctorDTO>> getListDoctor (@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody DoctorFilterDTO filter) {
        Pageable pageable = pageable (pageIndex, size, sort);

        List<DoctorDTO> doctorDTOs = doctorService.getListDoctor (pageable, filter);
        return ResponseEntity.ok(doctorDTOs);
    }

    private Pageable pageable(Integer pageIndex, Integer size, String sort) {
        return PageRequest.of(pageIndex, size, Sort.unsorted());
    }


}
