package org.example.api;

import org.example.dto.PatientDTO;
import org.example.dto.PatientFilterDTO;
import org.example.exceptions.NotFound;
import org.example.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientApi {
    public static final String API_PATIENT_ID = "/api/patient/{id}";
    public static final String API_PATIENT = "/api/patient";
    public static final String API_PATIENT_FILTER = "/api/patient/filter";
    @Autowired
    private PatientService patientService;

    @PostMapping (API_PATIENT)
    ResponseEntity<PatientDTO> createPatient (@RequestBody PatientDTO patientDTO) {
        patientDTO = patientService.createPatient(patientDTO);
        return ResponseEntity.ok(patientDTO);
    }

    @PostMapping (API_PATIENT_FILTER)
    ResponseEntity<List<PatientDTO>> getListPatient (@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody PatientFilterDTO filter) {
        Pageable pageable = pageable (pageIndex, size, sort);

        List<PatientDTO> patientDTOs = patientService.getListPatient (pageable, filter);
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping(API_PATIENT_ID)
    ResponseEntity<PatientDTO> getPatient (@PathVariable Long id) {
        try {
            PatientDTO dto = patientService.getPatient(id);
            return ResponseEntity.ok(dto);
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(API_PATIENT_ID)
    ResponseEntity<Void> updatePatient (@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        try {
            patientDTO.setId(id);
            patientService.updatePatient (patientDTO);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(API_PATIENT_ID)
    ResponseEntity<Void> deletePatient (@PathVariable Long id) {
        try {
            patientService.deletePatient (id);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
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