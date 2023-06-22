package rookie.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import rookie.dto.DoctorDTO;
import rookie.dto.DoctorFilterDTO;
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
        String expectedName = "Pietro";
        requestDTO.setName(expectedName);
        String expectedSurname = "Saccomando";
        requestDTO.setSurname(expectedSurname);
        String expectedEmail = "pietro.saccomando@gmail.com";
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
        Long id = responseDTO.getId();
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
        List<DoctorDTO> doctorDTOList = jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(ArrayList.class, DoctorDTO.class));
        Assertions.assertNotNull(doctorDTOList);
        Assertions.assertEquals(2, doctorDTOList.size(), "doctorDTOList size");

        DoctorDTO doctorDTO_uno = doctorDTOList.get(0);
        Assertions.assertEquals(expectedSurname, doctorDTO_uno.getSurname(), "doctorDTO_uno surname");
        Assertions.assertEquals(expectedProfession1, doctorDTO_uno.getProfession(), "doctorDTO_uno profession");

        DoctorDTO doctorDTO_due = doctorDTOList.get(1);
        Assertions.assertEquals(expectedSurname, doctorDTO_due.getSurname(), "doctorDTO_due surname");
        Assertions.assertEquals(expectedProfession2, doctorDTO_due.getProfession(), "doctorDTO_due profession");
    }

    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void getDoctorTest() throws Exception{
        final Long id = 3L;
        final String name = "Pippo";
        final String surname = "Testsurname";
        final String phoneNumber = "444 7777777";
        final String email = "pippo.testsurname@gmail.com";
        final String profession = "Arrotino";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(DoctorApi.API_DOCTOR_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        DoctorDTO doctorDTO = jsonMapper.readValue(jsonResponse, DoctorDTO.class);
        Assertions.assertEquals(id, doctorDTO.getId(), "id");
        Assertions.assertEquals(name, doctorDTO.getName(), "name");
        Assertions.assertEquals(surname, doctorDTO.getSurname(), "surname");
        Assertions.assertEquals(phoneNumber, doctorDTO.getPhoneNumber(), "phone_number");
        Assertions.assertEquals(email, doctorDTO.getEmail(), email);
        Assertions.assertEquals(profession, doctorDTO.getProfession(), "profession");
    }

    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void upgradeDoctorTest() throws Exception{
        final Long id = 3L;
        final String name = "Pippo";
        final String surname = "Testsurname";
        final String phoneNumber = "444 7777777";
        final String email = "pippo.testsurname@gmail.com";
        final String intialProfession = "Arrotino";
        final String profession = "Calzolaio";

        String currentProfession = db.queryForObject("select profession from doctor where id = ?", String.class, id);
        Assertions.assertEquals(intialProfession, currentProfession, "initial profession");

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName(name);
        doctorDTO.setSurname(surname);
        doctorDTO.setPhoneNumber(phoneNumber);
        doctorDTO.setEmail(email);
        doctorDTO.setProfession(profession);
        String jsonRequest = jsonMapper.writeValueAsString(doctorDTO);

        mvc.perform(MockMvcRequestBuilders
                .put(DoctorApi.API_DOCTOR_ID, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        currentProfession = db.queryForObject("select profession from doctor where id = ?", String.class, id);
        Assertions.assertEquals(profession, currentProfession, "initial profession");
    }

    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void deleteDoctorTest() throws Exception{
        final Long id = 3L;

        int count = db.queryForObject("select count (*) from doctor where id = ?", Integer.class, id);
        Assertions.assertEquals(1, count, "doctor exists");


        mvc.perform(MockMvcRequestBuilders
                .delete(DoctorApi.API_DOCTOR_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        count = db.queryForObject("select count (*) from doctor where id = ?", Integer.class, id);
        Assertions.assertEquals(0, count, "doctor does not exists");
    }
    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}