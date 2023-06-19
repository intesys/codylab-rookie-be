package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.PatientRecordDTO;
import it.intesys.codylab.rookie.exceptions.NotFound;
import it.intesys.codylab.rookie.service.PatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PatientRecordApi extends RookieApi {
    public static final String API_PATIENT_RECORD_ID = "/api/patientRecord/{id}";
    public static final String API_PATIENT_RECORD = "/api/patientRecord";
    public static final String API_PATIENT_RECORD_FILTER = "/api/patientRecord/filter";
    @Autowired
    private PatientRecordService patientRecordService;

    @PostMapping (API_PATIENT_RECORD)
    ResponseEntity<PatientRecordDTO> createPatientRecord (@RequestBody PatientRecordDTO patientRecordDTO) {
        patientRecordDTO = patientRecordService.createPatientRecord(patientRecordDTO);
        return ResponseEntity.ok(patientRecordDTO);
    }

    @GetMapping(API_PATIENT_RECORD_ID)
    ResponseEntity<PatientRecordDTO> getPatientRecord (@PathVariable Long id) {
        try {
            PatientRecordDTO dto = patientRecordService.getPatientRecord(id);
            return ResponseEntity.ok(dto);
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(API_PATIENT_RECORD_ID)
    ResponseEntity<Void> updatePatientRecord (@PathVariable Long id, @RequestBody PatientRecordDTO patientRecordDTO) {
        try {
            patientRecordDTO.setId(id);
            patientRecordService.updatePatientRecord (patientRecordDTO);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(API_PATIENT_RECORD_ID)
    ResponseEntity<Void> deletePatientRecord (@PathVariable Long id) {
        try {
            patientRecordService.deletePatientRecord (id);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


}
