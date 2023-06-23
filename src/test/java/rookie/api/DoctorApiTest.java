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
import rookie.dto.PatientDTO;
import rookie.repository.RookieRepository;

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
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void getListDoctorTest () throws Exception {
        String expectedSurname = "Testsurname";
        String expectedProfession1 = "Nullafacente";
        String expectedProfession2 = "Calciatore";

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

        List<DoctorDTO> doctorDTOList = jsonMapper.readValue(jsonResponse, jsonMapper.getTypeFactory().constructCollectionType(ArrayList.class, DoctorDTO.class));
        Assertions.assertNotNull(doctorDTOList);
        Assertions.assertEquals(2, doctorDTOList.size(), "doctorDTOList size");
        DoctorDTO nullafacente = doctorDTOList.get(0);
        Assertions.assertEquals(expectedSurname, nullafacente.getSurname(), "nullafacente surname");
        Assertions.assertEquals(expectedProfession1, nullafacente.getProfession(), "nullafacente profession");
        DoctorDTO calciatore = doctorDTOList.get(1);
        Assertions.assertEquals(expectedSurname, calciatore.getSurname(), "calciatore surname");
        Assertions.assertEquals(expectedProfession2, calciatore.getProfession(), "calciatore profession");

        List<PatientDTO> calciatorePatientDTOList = calciatore.getLatestPatients();
        Assertions.assertEquals(RookieRepository.LATEST_RECORD_SIZE, calciatorePatientDTOList.size(), "Ultimi pazienti del calciatore");
        Assertions.assertFalse(calciatorePatientDTOList.stream()
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
                        .get(DoctorApi.API_DOCTOR_ID, id))
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

        List<PatientDTO> arrotinoPatientDTOList = arrotino.getLatestPatients();
        Assertions.assertEquals(2, arrotinoPatientDTOList.size(), "Ultimi pazienti del calciatore");
        Assertions.assertEquals(31L, arrotinoPatientDTOList.get(0).getId(), "Id del paziente");
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