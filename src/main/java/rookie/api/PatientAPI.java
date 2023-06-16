package rookie.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rookie.dto.PatientDTO;
import rookie.dto.PatientFilterDTO;
import rookie.exeptions.NotFound;
import rookie.service.PatientService;

import java.util.List;

@RestController
public class PatientAPI extends RookieAPI {
    @Autowired
    private PatientService patientService;

    @PostMapping ("/api/patient")
    ResponseEntity<PatientDTO> createPatient (@RequestBody PatientDTO patientDTO) {
        patientDTO = patientService.createPatient(patientDTO);
        return ResponseEntity.ok(patientDTO);
    }

    @PostMapping ("/api/patient/filter")
    ResponseEntity<List<PatientDTO>> getListPatient (@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody PatientFilterDTO filter) {
        Pageable pageable = pageable (pageIndex, size, sort);

        List<PatientDTO> patientDTOs = patientService.getListPatient (pageable, filter);
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping("/api/patient/{id}")
    ResponseEntity<PatientDTO> getPatient (@PathVariable Long id) {
        try {
            PatientDTO dto = patientService.getPatient(id);
            return ResponseEntity.ok(dto);
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/patient/{id}")
    ResponseEntity<Void> updatePatient (@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        try {
            patientDTO.setId(id);
            patientService.updatePatient(patientDTO);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/patient/{id}")
    ResponseEntity<Void> deletePatient (@PathVariable Long id) {
        try {
            patientService.deletePatient(id);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


}
