package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class DoctorApi {
    @Autowired
    private DoctorService doctorService;
    @PostMapping("api/doctor")
    ResponseEntity <DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO){
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return  ResponseEntity.ok(doctorDTO);
    }

}
