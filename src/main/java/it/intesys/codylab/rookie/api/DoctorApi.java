package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/*
    https://spring.io/guides/tutorials/rest/
 */
@RestController
public class DoctorApi {
    public static final String API_DOCTOR = "/api/doctor";
    @Autowired
    DoctorService doctorService;

    @PostMapping (API_DOCTOR)
    public ResponseEntity<DoctorDTO> createDoctor (@RequestBody DoctorDTO doctorDTO) {
        doctorDTO = doctorService.createDoctor (doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

}
