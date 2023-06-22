package it.intesys.codylab.rookie.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.dto.PatientFilterDTO;
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
        String expectedName = "Carlo";
        requestDTO.setName(expectedName);
        String expectedSurname = "Marchiori";
        requestDTO.setSurname(expectedSurname);
        String expectedEmail = "carlo.marchiori@gmail.com";
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
    void getListPatientTest () throws Exception {
        String expectedSurname = "Pavarana";
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

        List<PatientDTO> patientDTOs = jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(ArrayList.class, PatientDTO.class));
        Assertions.assertNotNull(patientDTOs);
        Assertions.assertEquals(2, patientDTOs.size(), "patientDTOs size");
        PatientDTO patientDTO1 = patientDTOs.get(0);
        Assertions.assertEquals(expectedSurname, patientDTO1.getSurname(), "patientDTO1 surname");
        Assertions.assertEquals(expectedName1, patientDTO1.getName(), "patientDTO1 name");
        PatientDTO patientDTO2 = patientDTOs.get(1);
        Assertions.assertEquals(expectedSurname, patientDTO2.getSurname(), "patientDTO2 surname");
        Assertions.assertEquals(expectedName2, patientDTO2.getName(), "patientDTO2 name");
    }

    @Test
    @Sql(scripts = "/sql/patients.sql")
    void getPatientTest () throws Exception {
        final Long id = 31L;
        final String name = "Qua";
        final String surname = "Pavarana";
        final String email = "franco.pavarana@gmail.com";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(PatientApi.API_PATIENT_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientDTO patientDTO = jsonMapper.readValue(jsonResponse, PatientDTO.class);
        Assertions.assertEquals(id, patientDTO.getId(), "id");
        Assertions.assertEquals(name, patientDTO.getName(), "name");
        Assertions.assertEquals(surname, patientDTO.getSurname(), "surname");
        Assertions.assertEquals(email, patientDTO.getEmail(), "email");
    }

    @Test
    @Sql(scripts = "/sql/patients.sql")
    void updatePatientTest () throws Exception {
        final Long id = 31L;
        final String name = "Qua";
        final String surname = "Pavarana";
        final String email = "franco.pavarana@gmail.com";

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
    void deletePatientTest () throws Exception {
        final Long id = 31L;

        Integer count = db.queryForObject("select count (*) from patient where id = ?", Integer.class, id);
        Assertions.assertEquals(1, count, "Patient exists");

        mvc.perform(MockMvcRequestBuilders
                .delete(PatientApi.API_PATIENT_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        count = db.queryForObject("select count (*) from patient where id = ?", Integer.class, id);
        Assertions.assertEquals(0, count, "Patient does not exist");
    }

    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}

