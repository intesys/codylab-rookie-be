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
    @Autowired
    ObjectMapper jsonMapper;
    @Autowired
    JdbcTemplate db;


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




    //qua sei andato a modificare doctors.sql nel numero 7, mettendo "nullafacente" al posto di "programmatore", superando cosi il test getListDoctorTest
    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void getListDoctorTest() throws  Exception{
        String expectedSurname = "Testsurname";
        String expectedProfession1 = "Calciatore";
        String expectedProfession2 = "Nullafacente";

        DoctorFilterDTO doctorFilterDTO = new DoctorFilterDTO();
        doctorFilterDTO.setSurname(expectedSurname);
        String jsonRequest = jsonMapper.writeValueAsString(doctorFilterDTO);

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
        List<DoctorDTO> doctorDTOs= jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(List.class, DoctorDTO.class));
        Assertions.assertNotNull(doctorDTOs);
        Assertions.assertEquals(2, doctorDTOs.size(), "doctorDTOs size");
        DoctorDTO doctorDTO1 = doctorDTOs.get(0);
        Assertions.assertEquals(expectedSurname, doctorDTO1.getSurname(), "doctorDTO1 surname");
        Assertions.assertEquals(expectedProfession1, doctorDTO1.getProfession(), "doctorDTO1 profession");
        DoctorDTO doctorDTO2 = doctorDTOs.get(1);
        Assertions.assertEquals(expectedSurname, doctorDTO2.getSurname(), "doctorDTO2 surname");
        Assertions.assertEquals(expectedProfession2, doctorDTO2.getProfession(), "doctorDTO2 profession");
    }

    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void getDoctorTest() throws  Exception{
        Long id = 1L;
        String name = "Rafaello";
        String surname = "Testsurname";
        String phoneNumber = "444 7777777";
        String email = "rafaello.marchiori@gmail.com";
        String profession = "Scalpellino";
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get(DoctorApi.API_DOCTOR_ID, id)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        DoctorDTO doctorDTO = jsonMapper.readValue(jsonResponse, DoctorDTO.class);
        Assertions.assertEquals(id, doctorDTO.getID(), "id");
        Assertions.assertEquals(name, doctorDTO.getName(), "name");
        Assertions.assertEquals(surname, doctorDTO.getSurname(), "surname");
        Assertions.assertEquals(phoneNumber, doctorDTO.getPhoneNumber(), "phoneNumber");
        Assertions.assertEquals(email, doctorDTO.getEmail(), "email");
        Assertions.assertEquals(profession, doctorDTO.getProfession(), "profession");

    }


    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void updateDoctorTest() throws  Exception{
        Long id = 1L;
        String name="Rafaello";
        String surname = "Testsurname";
        String phoneNumber = "444 7777777";
        String email = "rafaello.marchiori@gmail.com";
        String initialProfesison = "Scalpellino";
        String profession = "Pugile";
        String currentProfession = db.queryForObject("select profession from doctor where id =?", String.class, id);
        Assertions.assertEquals(initialProfesison, currentProfession,"initiali profession");

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName(name);
        doctorDTO.setSurname(surname);
        doctorDTO.setPhoneNumber(phoneNumber);
        doctorDTO.setEmail(email);
        doctorDTO.setProfession(profession);

        String jsonRequest = jsonMapper.writeValueAsString(doctorDTO);
        mvc.perform(MockMvcRequestBuilders.put(DoctorApi.API_DOCTOR_ID, id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        currentProfession = db.queryForObject("select profession from doctor where id= ?",String.class, id );
        Assertions.assertEquals(profession,currentProfession,"initial profession");
    }

    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void deleteDoctorTest() throws  Exception{
        Long id=3L;
        Integer count = db.queryForObject("select count (*) from doctor where id= ?", Integer.class, id);
        Assertions.assertEquals(1,count,"Doctor exists");

        mvc.perform(MockMvcRequestBuilders.delete(DoctorApi.API_DOCTOR_ID,id)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        count = db.queryForObject("select count (*) from doctor where id= ?", Integer.class, id);
        Assertions.assertEquals(0, count, "Doctor doesn't exist");
    }




    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}

