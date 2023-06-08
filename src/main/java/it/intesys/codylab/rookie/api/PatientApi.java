package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.dto.PatientFilterDTO;
import it.intesys.codylab.rookie.service.PatientService;
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

@RestController
public class PatientApi {
    public static final String API_DOCTOR_FILTER = "/api/patient/filter";
    public static final String API_DOCTOR = "/api/patient";
    @Autowired
    PatientService patientService;

    @PostMapping(API_DOCTOR)
    public ResponseEntity<PatientDTO> createPatient (@RequestBody PatientDTO patientDTO) {
        patientDTO = patientService.createPatient (patientDTO);
        return ResponseEntity.ok(patientDTO);
    }

    @PostMapping(API_DOCTOR_FILTER)
    public ResponseEntity<List<PatientDTO>> getListPatient (@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("sort") String sort, @RequestBody PatientFilterDTO filter) {
        Pageable pageable = PaginationUtil.buildPageable(page, size, defaultSort(sort));

        Page<PatientDTO> patientDTOs = patientService.getListPatient (pageable, filter);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(patientDTOs, API_DOCTOR_FILTER);
        return new ResponseEntity<>(patientDTOs.getContent(), headers, HttpStatus.OK);
    }

    private String defaultSort(String sort) {
        if (sort == null || sort.isBlank())
            return "surname,asc";
        return sort;
    }

}
