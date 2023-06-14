package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.service.DoctorService;
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

    /*
        campo
        campo,direzione
     */
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
