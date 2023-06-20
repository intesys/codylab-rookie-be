package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import rookie.dto.PatientDTO;
import org.springframework.http.ResponseEntity;
import rookie.exceptions.NotFound;
import rookie.service.PatientService;
import rookie.dto.PatientFilterDTO;

import java.util.List;

@RestController
public class PatientApi extends RookieApi {
    public static final String API_PATIENT_ID = "api/patient/{id}";
    public static final String API_PATIENT_FILTER = "/api/patient/filter";
    public static final String API_PATIENT = "/api/patient";
    @Autowired
    private PatientService patientService;

    @PostMapping(API_PATIENT)
    ResponseEntity<PatientDTO> createPatient(@RequestBody PatientDTO patientDTO){
        patientDTO = patientService.createPatient(patientDTO);
        return ResponseEntity.ok(patientDTO);
    }

    @PostMapping (API_PATIENT_FILTER)
    ResponseEntity<List<PatientDTO>> getListPatient(@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody PatientFilterDTO filter){
        Pageable pageable = pageable(pageIndex, size, sort);
        List<PatientDTO> patientDTOs= patientService.getListPatient(pageable, filter);
        return ResponseEntity.ok(patientDTOs);
    }

    @GetMapping(API_PATIENT_ID)
    ResponseEntity<PatientDTO> getPatient (@PathVariable Long id){
        try{
            PatientDTO dto= patientService.getPatient(id);
            return ResponseEntity.ok(dto);
        }catch (NotFound e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(API_PATIENT_ID)
    ResponseEntity<Void> updatePatient(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
        try {
            patientDTO.setId(id);
            patientService.updatePatient(patientDTO);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(API_PATIENT_ID)
    ResponseEntity<Void> deletePatient(@PathVariable Long id){
        try{
            patientService.deletePatient(id);
            return ResponseEntity.ok().build();
        }catch(NotFound e){
            return ResponseEntity.notFound().build();
        }

    }
}
