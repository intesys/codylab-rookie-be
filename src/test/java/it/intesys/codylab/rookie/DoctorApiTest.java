package it.intesys.codylab.rookie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.codylab.rookie.api.DoctorApi;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.DoctorFilterDTO;
import it.intesys.codylab.rookie.dto.PatientDTO;
import it.intesys.codylab.rookie.repository.RookieRepository;
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
    ObjectMapper jsonMapper;
    @Autowired
    WebApplicationContext context;
    @Autowired
    JdbcTemplate db;


    @Test
    void createDoctorTest () throws Exception {
        DoctorDTO requestDTO = new DoctorDTO();
        String expectedName = "Carlo";
        requestDTO.setName(expectedName);
        String expectedSurname = "Marchiori";
        requestDTO.setSurname(expectedSurname);
        String expectedEmail = "carlo.marchiori@gmail.com";
        requestDTO.setEmail(expectedEmail);
        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result;
        result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/doctor")
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
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void getListDoctorTest () throws Exception {
        String expectedSurname = "Testsurname";
        String expectedProfession1 = "Nullafacente";
        String expectedProfession2 = "Calciatore";

        DoctorFilterDTO doctorFilterDTO = new DoctorFilterDTO();
        doctorFilterDTO.setSurname(expectedSurname);
        String jsonRequest = jsonMapper.writeValueAsString(doctorFilterDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/doctor/filter")
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
        DoctorDTO nullafacente = doctorDTOs.get(0);
        Assertions.assertEquals(expectedSurname, nullafacente.getSurname(), "nullafacente surname");
        Assertions.assertEquals(expectedProfession1, nullafacente.getProfession(), "nullafacente profession");
        DoctorDTO calciatore = doctorDTOs.get(1);
        Assertions.assertEquals(expectedSurname, calciatore.getSurname(), "calciatore surname");
        Assertions.assertEquals(expectedProfession2, calciatore.getProfession(), "calciatore profession");

        List<PatientDTO> calciatorePatientDTOs = calciatore.getLatestPatients();
        Assertions.assertEquals(RookieRepository.LATEST_RECORD_SIZE, calciatorePatientDTOs.size(), "Ultimi pazienti del calciatore");
        Assertions.assertFalse(calciatorePatientDTOs.stream()
                .map(PatientDTO::getId)
                .toList()
                .contains(5L), "Manca il paziente 5");
    }

    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void getDoctorTest () throws Exception {
        final Long id = 3L;
        final String name = "Rafaello";
        final String surname = "Testsurname";
        final String phoneNumber = "444 7777777";
        final String email = "rafaello.marchiori@gmail.com";
        final String profession = "Arrotino";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/doctor/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        DoctorDTO arrotino = jsonMapper.readValue(jsonResponse, DoctorDTO.class);
        Assertions.assertEquals(id, arrotino.getId(), "id");
        Assertions.assertEquals(name, arrotino.getName(), "name");
        Assertions.assertEquals(surname, arrotino.getSurname(), "surname");
        Assertions.assertEquals(phoneNumber, arrotino.getPhoneNumber(), "phoneNumber");
        Assertions.assertEquals(email, arrotino.getEmail(), "email");
        Assertions.assertEquals(profession, arrotino.getProfession(), "profession");

        List<PatientDTO> calciatorePatientDTOs = arrotino.getLatestPatients();
        Assertions.assertEquals(1, calciatorePatientDTOs.size(), "Ultimi pazienti del calciatore");
        Assertions.assertEquals(31L, calciatorePatientDTOs.get(0).getId(), "Id del paziente");
    }

    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void updateDoctorTest () throws Exception {
        final Long id = 3L;
        final String name = "Rafaello";
        final String surname = "Testsurname";
        final String phoneNumber = "444 7777777";
        final String email = "rafaello.marchiori@gmail.com";

        final String initialProfession = "Arrotino";
        final String profession = "Calzolaio";

        String currentProfession = db.queryForObject("select profession from doctor where id = ?", String.class, id);
        Assertions.assertEquals(initialProfession, currentProfession, "initial profession");

        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setName(name);
        doctorDTO.setSurname(surname);
        doctorDTO.setPhoneNumber(phoneNumber);
        doctorDTO.setEmail(email);
        doctorDTO.setProfession(profession);

        String jsonRequest = jsonMapper.writeValueAsString(doctorDTO);

        mvc.perform(MockMvcRequestBuilders.put("/api/doctor/{id}", id).contentType(MediaType.APPLICATION_JSON).content(jsonRequest)).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        currentProfession = db.queryForObject("select profession from doctor where id = ?", String.class, id);
        Assertions.assertEquals(profession, currentProfession, "initial profession");
    }


    @Test
    @Sql(scripts = "/sql/doctors.sql")
    void deleteDoctorTest () throws Exception {
        final Long id = 3L;

        Integer count = db.queryForObject("select count (*) from doctor where id = ?", Integer.class, id);
        Assertions.assertEquals(1, count, "Doctor exists");

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/doctor/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        count = db.queryForObject("select count (*) from doctor where id = ?", Integer.class, id);
        Assertions.assertEquals(0, count, "Doctor does not exist");
    }



    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}
