package it.intesys.codylab.rookie;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import it.intesys.codylab.rookie.dto.PatientRecordDTO;
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

import java.util.Map;

@SpringBootTest
 public class PatientRecordApiTest {
    private MockMvc mvc;
    @Autowired
    WebApplicationContext context;
    @Autowired
    ObjectMapper jsonMapper;
    @Autowired
    JdbcTemplate db;


    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql"})
    void createPatientRecordTest () throws Exception {
        Long expectedPatientId = 31L;
        Long expectedDoctorId = 3L;
        String expectedTypeVisit = "prima visita";
        String expectedReasonVisit = "sto mal";
        String expectedTreatmentMade = "niente";

        PatientRecordDTO requestDTO = new PatientRecordDTO();
        requestDTO.setPatientId(expectedPatientId);
        DoctorDTO expectedDoctorDTO = new DoctorDTO();
        expectedDoctorDTO.setId(expectedDoctorId);
        requestDTO.setDoctor(expectedDoctorDTO);
        requestDTO.setTypeVisit(expectedTypeVisit);
        requestDTO.setReasonVisit(expectedReasonVisit);
        requestDTO.setTreatmentMade(expectedTreatmentMade);
        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post("/api/patientRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientRecordDTO responseDTO = jsonMapper.readValue(jsonResponse, PatientRecordDTO.class);

        Long id = responseDTO.getId();
        Assertions.assertNotNull(id, "patientRecord id");
        Assertions.assertEquals(requestDTO.getTreatmentMade(), responseDTO.getTreatmentMade(), "patientRecord treatmentMade");

        Map<String, Object> patientRecordMap = db.queryForMap("select * from patient_record where id = ?", id);
        Long patientId = ((Number) patientRecordMap.get("patient_id")).longValue();
        Assertions.assertEquals(expectedPatientId, patientId, "patientId");
        Long doctorId = ((Number) patientRecordMap.get("doctor_id")).longValue();
        Assertions.assertEquals(expectedDoctorDTO.getId(), doctorId, "doctorId");
        String treatmentMade = (String) patientRecordMap.get("treatment_made");
        Assertions.assertEquals(expectedTreatmentMade, treatmentMade, "treatmentMade");
        String typeVisit = (String) patientRecordMap.get("type_visit");
        Assertions.assertEquals(expectedTypeVisit, typeVisit, "typeVisit");
        String reasonVisit = (String) patientRecordMap.get("reason_visit");
        Assertions.assertEquals(expectedReasonVisit, reasonVisit, "reasonVisit");
    }

    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void getPatientRecordTest () throws Exception {
        Long id = 1L;
        Long expectedPatientid = 31L;
        Long expectedDoctorId = 3L;
        String expectedTypeVisit = "prima visita";
        String expectedReasonVisit = "sto mal";
        String expectedTreatmentMade = "niente";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get("/api/patientRecord/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientRecordDTO patientRecordDTO = jsonMapper.readValue(jsonResponse, PatientRecordDTO.class);
        Assertions.assertEquals(id, patientRecordDTO.getId(), "id");
        Assertions.assertEquals(expectedPatientid, patientRecordDTO.getPatientId(), "patientId");
        Assertions.assertEquals(expectedDoctorId, patientRecordDTO.getDoctor().getId(), "doctorId");
        Assertions.assertEquals(expectedTypeVisit, patientRecordDTO.getTypeVisit(), "typeVisit");
        Assertions.assertEquals(expectedReasonVisit, patientRecordDTO.getReasonVisit(), "reasonVisit");
        Assertions.assertEquals(expectedTreatmentMade, patientRecordDTO.getTreatmentMade(), "treamentMade");
    }

    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void updatePatientRecordTest () throws Exception {
        Long id = 1L;
        Long expectedPatientId = 31L;
        Long expectedDoctorId = 3L;
        String expectedTypeVisit = "prima visita";
        String expectedReasonVisit = "sto mal";
        String expectedTreatmentMade = "niente";
        String newTreatmentMade = "qualcosa";

        String currentTreatmentMade = db.queryForObject("select treatment_made from patient_record where id = ?", String.class, id);
        Assertions.assertEquals(currentTreatmentMade, expectedTreatmentMade, "initial treament_made");

        PatientRecordDTO requestDTO = new PatientRecordDTO();
        requestDTO.setPatientId(expectedPatientId);
        DoctorDTO expectedDoctorDTO = new DoctorDTO();
        expectedDoctorDTO.setId(expectedDoctorId);
        requestDTO.setDoctor(expectedDoctorDTO);
        requestDTO.setTypeVisit(expectedTypeVisit);
        requestDTO.setReasonVisit(expectedReasonVisit);
        requestDTO.setTreatmentMade(newTreatmentMade);

        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        mvc.perform(MockMvcRequestBuilders
                        .put("/api/patientRecord/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        currentTreatmentMade = db.queryForObject("select treatment_made from patient_record where id = ?", String.class, id);
        Assertions.assertEquals(currentTreatmentMade, newTreatmentMade, "new treament_made");
    }

    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void deletePatientRecordTest () throws Exception {
        final Long id = 1L;

        Integer count = db.queryForObject("select count (*) from patient_record where id = ?", Integer.class, id);
        Assertions.assertEquals(1, count, "PatientRecord exists");

        mvc.perform(MockMvcRequestBuilders
                        .delete("/api/patientRecord/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        count = db.queryForObject("select count (*) from patient_record where id = ?", Integer.class, id);
        Assertions.assertEquals(0, count, "PatientRecord does not exist");
    }

    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}

