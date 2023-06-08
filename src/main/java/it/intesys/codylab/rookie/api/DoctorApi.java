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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
/*
    https://spring.io/guides/tutorials/rest/
 */
@RestController
public class DoctorApi {
    public static final String API_DOCTOR = "/api/doctor";
    public static final String API_DOCTOR_FILTER = "/api/doctor/filter";

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
}
