package it.intesys.codylab.rookie.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.intesys.codylab.rookie.App;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class PatientRecordTest {
    public static final String REASON_VISIT = "G'ho mal de pansa";
    public static final String TREATMENT_MADE = "Purga";
    public static final String TYPE_VISIT = "Ambulatorio";
    public static final String REASON_VISIT2 = "G'ho mal de stomegho";
    public static final String TREATMENT_MADE2 = "Lavanda gastrica";
    public static final String TYPE_VISIT2 = "Sala operatoria";
    @Autowired
    private WebApplicationContext applicationContext;
    private final ObjectMapper objectMapper = JsonMapper.builder ().addModule(new JavaTimeModule()).build();
    private MockMvc mockMvc;
    private DoctorDTO doctor;
    private PatientDTO patient;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }


    @Test
    public void testCreatePatientRecord() throws Exception {
        doctor = DoctorTest.createDoctor(DoctorTest.newDoctor(), objectMapper, mockMvc);
        patient = PatientTest.createPatient(PatientTest.newPatient(List.of(doctor)), objectMapper, mockMvc);

        OffsetDateTime date = OffsetDateTime.now();
        PatientRecordDTO patientRecord = createPatientRecord(patient, doctor, date);

        assertEquals(patient.getId(), patientRecord.getPatientId());
        assertEquals(doctor.getId(), patientRecord.getDoctor().getId());
        assertEquals(REASON_VISIT, patientRecord.getReasonVisit());
        assertEquals(TREATMENT_MADE, patientRecord.getTreatmentMade());
        assertEquals(TYPE_VISIT, patientRecord.getTypeVisit());
        assertEquals(date.toInstant().truncatedTo(ChronoUnit.MILLIS), patientRecord.getDate().toInstant().truncatedTo(ChronoUnit.MILLIS));
    }



    @Test
    public void testUpdatePatientRecord() throws Exception {
        doctor = DoctorTest.createDoctor(DoctorTest.newDoctor(), objectMapper, mockMvc);
        patient = PatientTest.createPatient(PatientTest.newPatient(List.of(doctor)), objectMapper, mockMvc);

        OffsetDateTime date = OffsetDateTime.now();
        PatientRecordDTO patientRecord = createPatientRecord(patient, doctor, date);

        patientRecord.setReasonVisit(REASON_VISIT2);
        patientRecord.setTreatmentMade(TREATMENT_MADE2);
        patientRecord.setTypeVisit(TYPE_VISIT2);

        Long id = patientRecord.getId();
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/api/patientRecord/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientRecord))).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        String content = response.getContentAsString();
        assertEquals("", content);

        patientRecord = getPatientRecord(id);

        assertEquals(id, patientRecord.getId());
        assertEquals(REASON_VISIT2, patientRecord.getReasonVisit());
        assertEquals(TREATMENT_MADE2, patientRecord.getTreatmentMade());
        assertEquals(TYPE_VISIT2, patientRecord.getTypeVisit());
    }

    public PatientRecordDTO createPatientRecord (PatientDTO patient, DoctorDTO doctor, OffsetDateTime now) throws Exception {
        PatientRecordDTO patientRecord = newPatientRecord(patient, doctor, now);

        return createPatientRecord(patientRecord);
    }

    public PatientRecordDTO createPatientRecord (PatientRecordDTO patientRecord) throws Exception {
        return createPatientRecord(patientRecord, objectMapper, mockMvc);
    }
    @NotNull
    public static PatientRecordDTO newPatientRecord(PatientDTO patient, DoctorDTO doctor, OffsetDateTime now) {
        PatientRecordDTO patientRecord = new PatientRecordDTO();

        patientRecord.setPatientId(patient.getId());
        patientRecord.setDoctor(doctor);
        patientRecord.setDate(now);
        patientRecord.setReasonVisit(REASON_VISIT);
        patientRecord.setTreatmentMade(TREATMENT_MADE);
        patientRecord.setTypeVisit(TYPE_VISIT);

        return patientRecord;
    }


    public static PatientRecordDTO createPatientRecord(PatientRecordDTO patientRecord, ObjectMapper objectMapper, MockMvc mockMvc) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/api/patientRecord")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patientRecord))).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        return objectMapper.readValue(response.getContentAsString(), PatientRecordDTO.class);
    }


    @Test
    public void testGetPatientRecord() throws Exception {
        doctor = DoctorTest.createDoctor(DoctorTest.newDoctor(), objectMapper, mockMvc);
        patient = PatientTest.createPatient(PatientTest.newPatient(List.of(doctor)), objectMapper, mockMvc);

        OffsetDateTime date = OffsetDateTime.now();
        PatientRecordDTO patientRecord = createPatientRecord(patient, doctor, date);
        patientRecord = getPatientRecord(patientRecord.getId());

        assertEquals(patient.getId(), patientRecord.getPatientId());
        assertEquals(doctor.getId(), patientRecord.getDoctor().getId());
        assertEquals(REASON_VISIT, patientRecord.getReasonVisit());
        assertEquals(TREATMENT_MADE, patientRecord.getTreatmentMade());
        assertEquals(TYPE_VISIT, patientRecord.getTypeVisit());
        assertEquals(date.toInstant().truncatedTo(ChronoUnit.MILLIS), patientRecord.getDate().toInstant().truncatedTo(ChronoUnit.MILLIS));
    }

    @Test
    public void testDeletePatientRecord() throws Exception {
        doctor = DoctorTest.createDoctor(DoctorTest.newDoctor(), objectMapper, mockMvc);
        patient = PatientTest.createPatient(PatientTest.newPatient(List.of(doctor)), objectMapper, mockMvc);

        OffsetDateTime date = OffsetDateTime.now();
        PatientRecordDTO patientRecord = createPatientRecord(patient, doctor, date);

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/patientRecord/" + patientRecord.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/patientRecord/" + patientRecord.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/patientRecord/" + patientRecord.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 404);
    }

    private PatientRecordDTO getPatientRecord(Long id) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/patientRecord/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        PatientRecordDTO patientRecord = objectMapper.readValue(response.getContentAsString(), PatientRecordDTO.class);
        return patientRecord;
    }
}
