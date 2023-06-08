package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
/*
    https://spring.io/guides/tutorials/rest/
 */
@RestController
public class DoctorApi {
    public static final String API_DOCTOR = "/api/doctor";
    public static final String API_DOCTOR_FILTER = "/api/doctor/filter";
    public static final String API_DOCTOR_ID = "/api/doctor/{id}";

    @Autowired
    DoctorService doctorService;

    @PostMapping (API_DOCTOR)
    public ResponseEntity<DoctorDTO> createDoctor (@RequestBody DoctorDTO doctorDTO) {
        doctorDTO = doctorService.createDoctor (doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping(API_DOCTOR_FILTER)
    public ResponseEntity<List<DoctorDTO>> getListDoctor (@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestBody DoctorFilterDTO filter) {
        Pageable pageable = PaginationUtil.buildPageable(page, size, defaultSort (sort));

        Page<DoctorDTO> doctorDTOs = doctorService.getListDoctor (pageable, filter);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(doctorDTOs, API_DOCTOR_FILTER);
        return new ResponseEntity<>(doctorDTOs.getContent(), headers, HttpStatus.OK);
    }

    private String defaultSort(String sort) {
        if (sort == null || sort.isBlank())
            return "surname,asc";
        return sort;
    }


    @PutMapping(API_DOCTOR_ID)
    public ResponseEntity<Void> updateDoctor (@PathVariable("id") Long id, @RequestBody DoctorDTO doctorDTO) {
        doctorDTO.setId(id);
        doctorService.updateDoctor (doctorDTO);
        return ResponseEntity.ok(null);
    }

    @GetMapping(API_DOCTOR_ID)
    public ResponseEntity<DoctorDTO> getDoctor (@PathVariable("id") Long id) {
        DoctorDTO doctorDTO = doctorService.getDoctor (id);
        return ResponseEntity.ok(doctorDTO);
    }

    @DeleteMapping(API_DOCTOR_ID)
    public ResponseEntity<Void> deleteDoctor (@PathVariable("id") Long id) {
        doctorService.deleteDoctor (id);
        return ResponseEntity.ok(null);
    }

}
