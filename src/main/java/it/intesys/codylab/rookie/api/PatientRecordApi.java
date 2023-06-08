package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.PatientRecordDTO;
import it.intesys.codylab.rookie.service.PatientRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
    https://spring.io/guides/tutorials/rest/
 */
@RestController ()
public class PatientRecordApi {
    public static final String API_DOCTOR = "/api/patientRecord";
    @Autowired
    PatientRecordService patientRecordService;

    @PostMapping (API_DOCTOR)
    public ResponseEntity<PatientRecordDTO> createPatientRecord (@RequestBody PatientRecordDTO patientRecordDTO) {
        patientRecordDTO = patientRecordService.createPatientRecord (patientRecordDTO);
        return ResponseEntity.ok(patientRecordDTO);
    }
}
