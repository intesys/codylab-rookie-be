package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PatientApi {
    public static final String API_DOCTOR = "/api/patient";
    @Autowired
    PatientService patientService;

    @PostMapping(API_DOCTOR)
    public ResponseEntity<PatientDTO> createPatient (@RequestBody PatientDTO patientDTO) {
        patientDTO = patientService.createPatient (patientDTO);
        return ResponseEntity.ok(patientDTO);
    }
}
