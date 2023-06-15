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
    public static final String API_DOCTOR_ID = "/api/doctor/{id}";
    public static final String API_DOCTOR = "/api/doctor";
    public static final String API_DOCTOR_FILTER = "/api/doctor/filter";
    @Autowired
    private DoctorService doctorService;

    @PostMapping (API_DOCTOR)
    ResponseEntity<DoctorDTO> createDoctor (@RequestBody DoctorDTO doctorDTO) {
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping (API_DOCTOR_FILTER)
    ResponseEntity<List<DoctorDTO>> getListDoctor (@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody DoctorFilterDTO filter) {
        Pageable pageable = pageable (pageIndex, size, sort);

        List<DoctorDTO> doctorDTOs = doctorService.getListDoctor (pageable, filter);
        return ResponseEntity.ok(doctorDTOs);
    }

    @GetMapping(API_DOCTOR_ID)
    ResponseEntity<DoctorDTO> getDoctor (@PathVariable Long id) {
        DoctorDTO dto = doctorService.getDoctor (id);
        return ResponseEntity.ok(dto);
    }

    @PutMapping(API_DOCTOR_ID)
    void updateDoctor (@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
        doctorDTO.setId(id);
        doctorService.updateDoctor (doctorDTO);
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
