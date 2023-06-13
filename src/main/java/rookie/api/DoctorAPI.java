package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import rookie.dto.DoctorDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rookie.service.DoctorService;

@RestController
public class DoctorAPI {
    @Autowired
    private DoctorService doctorService;
    @PostMapping("/api/doctor")
    public ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO){
        DoctorDTO doctor = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctor);
    }
}
