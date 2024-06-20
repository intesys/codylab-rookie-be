package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.PatientRecordDTO;
import it.intesys.codylab.rookie.service.PatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/*
    https://spring.io/guides/tutorials/rest/
 */
@RestController ()
public class PatientRecordApi {
    public static final String API_DOCTOR_ID = "/api/patientRecord/{id}";
    public static final String API_DOCTOR = "/api/patientRecord";
    @Autowired
    PatientRecordService patientRecordService;

    @PostMapping (API_DOCTOR)
    public ResponseEntity<PatientRecordDTO> createPatientRecord (@RequestBody PatientRecordDTO patientRecordDTO) {
        patientRecordDTO = patientRecordService.createPatientRecord (patientRecordDTO);
        return ResponseEntity.ok(patientRecordDTO);
    }
    @PutMapping(API_DOCTOR_ID)
    public ResponseEntity<Void> updatePatientRecord (@PathVariable("id") Long id, @RequestBody PatientRecordDTO patientRecordDTO) {
        patientRecordDTO.setId(id);
        patientRecordService.updatePatientRecord (patientRecordDTO);
        return ResponseEntity.ok(null);
    }

    @GetMapping(API_DOCTOR_ID)
    public ResponseEntity<PatientRecordDTO> getPatientRecord (@PathVariable("id") Long id) {
        return Optional.ofNullable(patientRecordService.getPatientRecord (id))
                .map(ResponseEntity::ok)
                .orElse (ResponseEntity.notFound().build());
    }

    @DeleteMapping(API_DOCTOR_ID)
    public ResponseEntity<Void> deletePatientRecord (@PathVariable("id") Long id) {
        patientRecordService.deletePatientRecord (id);
        return ResponseEntity.ok(null);
    }
}
