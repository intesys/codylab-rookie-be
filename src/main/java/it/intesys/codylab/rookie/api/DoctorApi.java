package it.intesys.codylab.rookie.api;

import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping("/api/doctor/filter")
    ResponseEntity<List<DoctorDTO>>getListDoctor (@RequestParam("page")Integer page, @RequestParam("size") Integer size, @RequestParam("sort") String sort,  @RequestBody DoctorFilterDTO filter){
        Pageable pageable = pageable(page,size,sort);
        List<DoctorDTO> doctorDTOs = doctorService.getListDoctor(pageable, filter);
        return  ResponseEntity.ok(doctorDTOs);
    }
    private Pageable pageable(Integer pageIndex, Integer size, String sort){
        return PageRequest.of(pageIndex,size, Sort.unsorted());
    }

}
