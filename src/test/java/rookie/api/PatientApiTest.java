package rookie.api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import rookie.dto.PatientDTO;
import rookie.dto.PatientFilterDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Transactional
public class PatientApiTest {
    private MockMvc mvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper jsonMapper;
    @Autowired
    JdbcTemplate db;


    @Test
    void createPatientTest () throws Exception {
        PatientDTO requestDTO = new PatientDTO();
        String expectedName = "Pietro";
        requestDTO.setName(expectedName);
        String expectedSurname = "Saccomando";
        requestDTO.setSurname(expectedSurname);
        String expectedEmail = "pietro.saccomando@gmail.com";
        requestDTO.setEmail(expectedEmail);
        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .post(PatientApi.API_PATIENT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientDTO responseDTO = jsonMapper.readValue(jsonResponse, PatientDTO.class);
        Long id = responseDTO.getId();
        Assertions.assertNotNull(id, "patient id");
        Assertions.assertEquals(requestDTO.getName(), responseDTO.getName(), "patient name");

        Map<String, Object> patientMap = db.queryForMap("select * from patient where id = ?", id);
        String name = (String) patientMap.get("name");
        Assertions.assertEquals(expectedName, name, "name");

        String surname = (String) patientMap.get("surname");
        Assertions.assertEquals(expectedSurname, surname, "surname");

        String email = (String) patientMap.get("email");
        Assertions.assertEquals(expectedEmail, email, "email");
    }

    @Test
    @Sql(scripts = "/sql/patients.sql")
    void getListDoctorTest () throws Exception {
        String expectedSurname = "Test";
        String expectedName1 = "Qua";
        String expectedName2 = "Paperone";

        PatientFilterDTO patientFilterDTO = new PatientFilterDTO();
        patientFilterDTO.setText(expectedSurname);
        String jsonRequest = jsonMapper.writeValueAsString(patientFilterDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(PatientApi.API_PATIENT_FILTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .param("page", "1")
                        .param("size", "2")
                        .param("sort", "name,desc")
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        List<PatientDTO> patientDTOList = jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(ArrayList.class, PatientDTO.class));
        Assertions.assertNotNull(patientDTOList);
        Assertions.assertEquals(2, patientDTOList.size(), "patientDTOList size");

        PatientDTO patientDTO_uno = patientDTOList.get(0);
        Assertions.assertEquals(expectedSurname, patientDTO_uno.getSurname(), "patientDTO_uno surname");
        Assertions.assertEquals(expectedName1, patientDTO_uno.getName(), "patientDTO_uno name");

        PatientDTO patientDTO_due = patientDTOList.get(1);
        Assertions.assertEquals(expectedSurname, patientDTO_due.getSurname(), "patientDTO_due surname");
        Assertions.assertEquals(expectedName2, patientDTO_due.getName(), "patientDTO_due name");
    }

    @Test
    @Sql(scripts = "/sql/patients.sql")
    void getPatientTest() throws Exception{
        final Long id = 31L;
        final String name = "Qua";
        final String surname = "Test";
        final String email = "qua.test@gmail.com";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                .get(PatientApi.API_PATIENT_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientDTO doctorDTO = jsonMapper.readValue(jsonResponse, PatientDTO.class);
        Assertions.assertEquals(id, doctorDTO.getId(), "id");
        Assertions.assertEquals(name, doctorDTO.getName(), "name");
        Assertions.assertEquals(surname, doctorDTO.getSurname(), "surname");
        Assertions.assertEquals(email, doctorDTO.getEmail(), "email");
    }

    @Test
    @Sql(scripts = "/sql/patients.sql")
    void updateDoctorTest() throws Exception{
        final Long id = 31L;
        final String name = "Qua";
        final String surname = "Test";
        final String email = "qua.test@gmail.com";
        final Long phoneNumber = 045123456L;

        Long currentPhoneNumber = db.queryForObject("select phone_number from patient where id = ?", Long.class, id);
        Assertions.assertNull(currentPhoneNumber, "initial phoneNumber");

        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setName(name);
        patientDTO.setSurname(surname);
        patientDTO.setPhoneNumber(phoneNumber);
        patientDTO.setEmail(email);

        String jsonRequest = jsonMapper.writeValueAsString(patientDTO);

        mvc.perform(MockMvcRequestBuilders
                        .put(PatientApi.API_PATIENT_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        currentPhoneNumber = db.queryForObject("select phone_number from patient where id = ?", Long.class, id);
        Assertions.assertEquals(phoneNumber, currentPhoneNumber, "initial phoneNumber");
    }



    @Test
    @Sql(scripts = "/sql/patients.sql")
    void deleteDoctorTest() throws Exception {
        final Long id = 31L;

        int count = db.queryForObject("select count (*) from patient where id = ?", Integer.class, id);
        Assertions.assertEquals(1, count, "patient exists");


        mvc.perform(MockMvcRequestBuilders
                        .delete(PatientApi.API_PATIENT_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        count = db.queryForObject("select count (*) from patient where id = ?", Integer.class, id);
        Assertions.assertEquals(0, count, "patient does not exists");
    }


    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}