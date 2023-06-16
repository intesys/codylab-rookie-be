package rookie.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import rookie.dto.DoctorDTO;
import rookie.exceptions.NotFound;
import rookie.service.DoctorService;
import rookie.dto.DoctorFilterDTO;

import java.util.List;

@RestController
public class DoctorAPI extends RookieAPI{
    public static final String API_DOCTOR_ID = "api/doctor/{id}";
    public static final String API_DOCTOR_FILTER = "/api/doctor/filter";
    public static final String API_DOCTOR = "/api/doctor";
    @Autowired
    private DoctorService doctorService;

    @PostMapping(API_DOCTOR)
    ResponseEntity<DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO){
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return ResponseEntity.ok(doctorDTO);
    }

    @PostMapping (API_DOCTOR_FILTER)
    ResponseEntity<List<DoctorDTO>> getListDoctor(@RequestParam("page") Integer pageIndex, @RequestParam("size") Integer size, @RequestParam ("sort") String sort, @RequestBody DoctorFilterDTO filter){
        Pageable pageable = pageable(pageIndex, size, sort);
        List<DoctorDTO> doctorDTOs= doctorService.getListDoctor(pageable, filter);
        return ResponseEntity.ok(doctorDTOs);
    }

    @GetMapping(API_DOCTOR_ID)
    ResponseEntity<DoctorDTO> getDoctor (@PathVariable Long id){
        try{
            DoctorDTO dto= doctorService.getDoctor(id);
            return ResponseEntity.ok(dto);
        }catch (NotFound e){
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping(API_DOCTOR_ID)
    ResponseEntity<Void> updateDoctor(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO){
        try{
            doctorDTO.setId(id);
            doctorService.updateDoctor(doctorDTO);
            return ResponseEntity.ok().build();
        }catch(NotFound e){
            return ResponseEntity.notFound().build();
        }

    }

    @DeleteMapping(API_DOCTOR_ID)
    ResponseEntity<Void> deleteDoctor(@PathVariable Long id){
        try{
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok().build();
        }catch(NotFound e){
            return ResponseEntity.notFound().build();
        }

    }

}

