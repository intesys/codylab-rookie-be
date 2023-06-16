package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.domain.Doctor;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.exceptions.NotFound;
import it.intesys.codylab.rookie.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
public class DoctorApi extends  RookieApi{
    public static final String API_DOCTOR_ID = "/api/doctor/{id}";
    public static final String API_DOCTOR_FILTER = "/api/doctor/filter";
    public static final String API_DOCTOR = "api/doctor";
    @Autowired
    private DoctorService doctorService;
    @PostMapping(API_DOCTOR)
    ResponseEntity <DoctorDTO> createDoctor(@RequestBody DoctorDTO doctorDTO) throws NotFound {
        doctorDTO = doctorService.createDoctor(doctorDTO);
        return  ResponseEntity.ok(doctorDTO);
    }
    @PostMapping(API_DOCTOR_FILTER)
    ResponseEntity<List<DoctorDTO>>getListDoctor (@RequestParam("page")Integer page, @RequestParam("size") Integer size, @RequestParam("sort") String sort,  @RequestBody DoctorFilterDTO filter){
        Pageable pageable = pageable(page,size,sortString(sort));
        List<DoctorDTO> doctorDTOs = doctorService.getListDoctor(pageable, filter);
        return  ResponseEntity.ok(doctorDTOs);
    }

    //parte col punto di domanda viene chiamata quey
    @GetMapping(API_DOCTOR_ID)
    ResponseEntity <DoctorDTO> getDoctor (@PathVariable Long id){
        try {
            DoctorDTO dto = doctorService.getDoctor(id);
            return ResponseEntity.ok(dto);
        } catch (NotFound e){
            return  ResponseEntity.notFound().build();
        }
    }
    @PutMapping(API_DOCTOR_ID)
    ResponseEntity <Void> updateDoctor (@PathVariable Long id, @RequestBody DoctorDTO doctorDTO){
        try {
            doctorDTO.setID(id);
            doctorService.updateDoctor(doctorDTO);
            return ResponseEntity.ok().build();
        }catch (NotFound e){
        return  ResponseEntity.notFound().build();
    }

    }
    @DeleteMapping(API_DOCTOR_ID)
    ResponseEntity <Void> deleteDoctor ( @PathVariable Long id) {
        try {
            doctorService.deleteDoctor(id);
            return ResponseEntity.ok().build();
        } catch (NotFound e) {
            return ResponseEntity.notFound().build();
        }
    }


    //campo direzione
    public PageRequest pageable(Integer pageIndex, Integer size, String sort){
        //Sort.by(List.of (new Sort.Order(Sort.Direction.ASC, "pippo")));

        if(sort!=null && !sort.isBlank()){
            Sort.Order order;
            String [] sortSplit = sort.split(",");
            String field = sortSplit[0];
            String direction = sortSplit[1];

            if(sortSplit.length==2){
                order = new Sort.Order(Sort.Direction.fromString(direction), field);
            }else{
                order = Sort.Order.by(field);
            }
            return PageRequest.of(pageIndex,size, Sort.unsorted());
        }
        return PageRequest.of(pageIndex,size, Sort.unsorted());
    }

    private  String sortString(String sort){
        if(sort==null|| sort.isBlank()){
            return "surname,asc";
        }
        return sort;
    }
    public  void save(Doctor doctor){
        Long id= doctor.getId();
    }

}
