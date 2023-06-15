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
    ResponseEntity<DoctorDTO> getDoctor (@PathVariable Long id){
        DoctorDTO dto = doctorService.getDoctor(id);
        return ResponseEntity.ok(dto);
    }


    private Pageable pageable(Integer page, Integer size, String sort) {
        if (sort != null && !sort.isBlank()) {
            Sort.Order order;
            String[] sortSplit = sort.split(",");
            String valueField = sortSplit[0];
            String sortingField = sortSplit[1];

            if (sortSplit.length == 2) {
                order = new Sort.Order(Sort.Direction.fromString(sortingField), valueField);
            } else {
                order = Sort.Order.by(sortSplit[0]);
            }

            return PageRequest.of(page, size, Sort.by(order));
        } else {
            return PageRequest.of(page, size);
        }
    }


}