package rookie.api;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rookie.dto.PatientRecordDTO;
import rookie.exeptions.NotFound;
import rookie.service.PatientRecordService;

import java.util.List;

@RestController
public class PatientRecordApi extends RookieAPI {

    @Autowired
    private PatientRecordService patientRecordService;

    @PostMapping ("/api/patientRecord")
    ResponseEntity<PatientRecordDTO> createPatientRecord (@RequestBody PatientRecordDTO patientRecordDTO) {
        patientRecordDTO = patientRecordService.createPatientRecord(patientRecordDTO);
        return ResponseEntity.ok(patientRecordDTO);
    }

    @GetMapping("/api/patientRecord/{id}")
    ResponseEntity<PatientRecordDTO> getPatientRecord (@PathVariable Long id) {
        try {
            PatientRecordDTO dto = patientRecordService.getPatientRecord(id);
            return ResponseEntity.ok(dto);
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/api/patientRecord/{id}")
    ResponseEntity<Void> updatePatientRecord (@PathVariable Long id, @RequestBody PatientRecordDTO patientRecordDTO) {
        try {
            patientRecordDTO.setId(id);
            patientRecordService.updatePatientRecord (patientRecordDTO);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/api/patientRecord/{id}")
    ResponseEntity<Void> deletePatientRecord (@PathVariable Long id) {
        try {
            patientRecordService.deletePatientRecord (id);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


}
