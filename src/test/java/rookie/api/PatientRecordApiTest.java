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
import rookie.dto.PatientRecordDTO;
import rookie.dto.DoctorDTO;


import java.util.Map;

@SpringBootTest
@Transactional
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
        PatientRecordDTO requestDTO = new PatientRecordDTO();
        DoctorDTO doctorDTO = new DoctorDTO();
        Long expectedPatientId = 32L;
        Long expectedDoctorId = 2L;
        String expectedTypeVisit = "Prima visita";
        String expectedReasonVisit = "Mal di pancia";
        String expectedTreatmentMade = "Niente";

        requestDTO.setPatientId(expectedPatientId);
        doctorDTO.setId(expectedDoctorId);
        requestDTO.setDoctor(doctorDTO);
        requestDTO.setTypeVisit(expectedTypeVisit);
        requestDTO.setReasonVisit(expectedReasonVisit);
        requestDTO.setTreatmentMade(expectedTreatmentMade);

        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(PatientRecordApi.API_PATIENT_RECORD)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientRecordDTO responseDTO = jsonMapper.readValue(jsonResponse, PatientRecordDTO.class);
        Long id = responseDTO.getId();
        Assertions.assertNotNull(id, "patient record id");
        Assertions.assertEquals(requestDTO.getTreatmentMade(), responseDTO.getTreatmentMade(), "patient record treatment made");

        Map<String, Object> patientRecordMap = db.queryForMap("select * from patient_record where id = ?", id);
        Long patient_id = ((Number) patientRecordMap.get("patient_id")).longValue();
        Assertions.assertEquals(expectedPatientId, patient_id, "patientId");
        Long doctor_id = ((Number) patientRecordMap.get("doctor_id")).longValue();
        Assertions.assertEquals(expectedDoctorId, doctor_id, "doctorId");
        String typeVisit = (String) patientRecordMap.get("type_visit");
        Assertions.assertEquals(expectedTypeVisit, typeVisit, "typeVisit");
        String reasonVisit= (String) patientRecordMap.get("reason_visit");
        Assertions.assertEquals(expectedReasonVisit, reasonVisit, "reasonVisit");
        String treatmentMade= (String) patientRecordMap.get("treatment_made");
        Assertions.assertEquals(expectedTreatmentMade, treatmentMade, "treatmentMade");

    }


    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void getPatientRecordTest() throws Exception{
        Long id = 1L;
        Long expectedPatientId = 32L;
        Long expectedDoctorId = 2L;
        String expectedTypeVisit = "Prima visita";
        String expectedReasonVisit = "Mal di pancia";
        String expectedTreatmentMade = "Niente";

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .get(PatientRecordApi.API_PATIENT_RECORD_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientRecordDTO patientRecordDTO = jsonMapper.readValue(jsonResponse, PatientRecordDTO.class);
        Assertions.assertEquals(id, patientRecordDTO.getId(), "id");
        Assertions.assertEquals(expectedPatientId, patientRecordDTO.getPatientId(), "patient id");
        Assertions.assertEquals(expectedDoctorId, patientRecordDTO.getDoctor().getId(), "doctor id");
        Assertions.assertEquals(expectedTypeVisit, patientRecordDTO.getTypeVisit(), "type visit");
        Assertions.assertEquals(expectedReasonVisit, patientRecordDTO.getReasonVisit(), "reason visit");
        Assertions.assertEquals(expectedTreatmentMade, patientRecordDTO.getTreatmentMade(), "treatment made");
    }

    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void updatePatientRecordTest() throws Exception{
        Long id = 1L;
        Long expectedPatientId = 32L;
        Long expectedDoctorId = 2L;
        String expectedTypeVisit = "Prima visita";
        String expectedReasonVisit = "Mal di pancia";
        String expectedTreatmentMade = "Niente";
        String newTreatmentMade = "Visita";

        String currentTreatmentMade = db.queryForObject("select treatment_made from patient_record where id = ?", String.class, id);
        Assertions.assertEquals(currentTreatmentMade, expectedTreatmentMade, "initial treatmentMade");

        PatientRecordDTO patientRecordDTO = new PatientRecordDTO();
        patientRecordDTO.setPatientId(expectedPatientId);
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(expectedDoctorId);
        patientRecordDTO.setDoctor((doctorDTO));
        patientRecordDTO.setTypeVisit(expectedTypeVisit);
        patientRecordDTO.setReasonVisit(expectedReasonVisit);
        patientRecordDTO.setTreatmentMade(newTreatmentMade);

        String jsonRequest = jsonMapper.writeValueAsString(patientRecordDTO);

        mvc.perform(MockMvcRequestBuilders
                        .put(PatientRecordApi.API_PATIENT_RECORD_ID, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        currentTreatmentMade = db.queryForObject("select treatment_made from patient_record where id = ?", String.class, id);
        Assertions.assertEquals(currentTreatmentMade, newTreatmentMade, "new treatmentMade");
    }



    @Test
    @Sql(scripts = {"/sql/doctors.sql", "/sql/patients.sql", "/sql/patient_records.sql"})
    void deletePatientRecordTest() throws Exception {
        final Long id = 1L;

        int count = db.queryForObject("select count (*) from patient_record where id = ?", Integer.class, id);
        Assertions.assertEquals(1, count, "patient_record exists");


        mvc.perform(MockMvcRequestBuilders
                        .delete(PatientRecordApi.API_PATIENT_RECORD_ID, id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        count = db.queryForObject("select count (*) from patient_record where id = ?", Integer.class, id);
        Assertions.assertEquals(0, count, "patient_record does not exists");
    }



    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}