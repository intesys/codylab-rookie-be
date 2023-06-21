package it.intesys.codylab.rookie.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
public class DoctorApiTest {
    private MockMvc mvc;
    @Autowired
    WebApplicationContext context;
    ObjectMapper jsonMapper = new ObjectMapper();
    @Autowired
    JdbcTemplate db;




    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void getListDoctorTest () throws Exception {
        DoctorFilterDTO doctorFilterDTO = new DoctorFilterDTO();
        String expectedSurname = "Testsurname";
        doctorFilterDTO.setSurname(expectedSurname);
        String jsonRequest = jsonMapper.writeValueAsString(doctorFilterDTO);
        String expectedProfession1 = "Nullafacente";
        String expectedProfession2 = "Calciatore";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(DoctorApi.API_DOCTOR_FILTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "profession,desc")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        List<DoctorDTO> doctorDTOs = jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(ArrayList.class, DoctorDTO.class));
        Assertions.assertNotNull(doctorDTOs);
        Assertions.assertEquals(2, doctorDTOs.size(), "doctorDTOs size");
        DoctorDTO doctorDTO1 = doctorDTOs.get(0);
        Assertions.assertEquals(expectedSurname, doctorDTO1.getSurname(), "doctorDTO1 surname");
        Assertions.assertEquals(expectedProfession1, doctorDTO1.getProfession(), "doctorDTO1 profession");
        DoctorDTO doctorDTO2 = doctorDTOs.get(1);
        Assertions.assertEquals(expectedSurname, doctorDTO2.getSurname(), "doctorDTO2 surname");
        Assertions.assertEquals(expectedProfession2, doctorDTO2.getProfession(), "doctorDTO2 profession");
    }

    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
    @Test
    void createDoctorTest () throws Exception {
        DoctorDTO requestDTO = new DoctorDTO();
        String expectedName = "Matteo";
        requestDTO.setName(expectedName);
        String expectedSurname = "Ocneanu";
        requestDTO.setSurname(expectedSurname);
        String expectedEmail = "matteo.ocneanu@gmail.com";
        requestDTO.setEmail(expectedEmail);
        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(DoctorApi.API_DOCTOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        DoctorDTO responseDTO = jsonMapper.readValue(jsonResponse, DoctorDTO.class);

        Long id = responseDTO.getID();
        Assertions.assertNotNull(id, "doctor id");
        Assertions.assertEquals(requestDTO.getName(), responseDTO.getName(), "doctor name");

        Map<String, Object> doctorMap = db.queryForMap("select * from doctor where id = ?", id);
        String name = (String) doctorMap.get("name");
        Assertions.assertEquals(expectedName, name, "name");
        String surname = (String) doctorMap.get("surname");
        Assertions.assertEquals(expectedSurname, surname, "surname");
        String email = (String) doctorMap.get("email");
        Assertions.assertEquals(expectedEmail, email, "email");
    }

}
