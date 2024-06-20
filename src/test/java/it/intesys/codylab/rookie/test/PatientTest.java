package it.intesys.codylab.rookie.test;

import com.fasterxml.jackson.core.type.TypeReference;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class PatientTest {
    public static final String ADDRESS = "Via Roveggia";
    public static final String AVATAR = "base64 image";
    public static final String EMAIL = "carlo.marchiori@intesys.it";
    public static final String NAME = "Carlo";
    public static final Long PHONE_NUMBER = 6666666666L;
    public static final String PROFESSION = "IT";
    public static final String SURNAME = "Marchiori";
    public static final PatientDTO.BloodGroupEnum BLOOD_GROUP = PatientDTO.BloodGroupEnum.A_MINUS;
    public static final boolean CHRONIC_PATIENT = false;
    public static final long IDP = 1L;
    public static final String NOTE = "Che topo!";
    public static final long OPD = 2L;

    public static final String ADDRESS2 = "Via Albere";
    public static final String AVATAR2 = "base64 image 2";
    public static final String EMAIL2 = "enrico.costanzi@intesys.it";
    public static final String NAME2 = "Enrico";
    public static final Long PHONE_NUMBER2 = 7777777777L;
    public static final String PROFESSION2 = "IT Architect";
    public static final String SURNAME2 = "Costanzi";
    public static final PatientDTO.BloodGroupEnum BLOOD_GROUP2 = PatientDTO.BloodGroupEnum.ZERO_PLUS;
    public static final boolean CHRONIC_PATIENT2 = true;
    public static final long IDP2 = 3L;
    public static final String NOTE2 = "Che tipo!";
    public static final long OPD2 = 4L;
    @Autowired
    private WebApplicationContext applicationContext;
    private final ObjectMapper objectMapper = JsonMapper.builder ().addModule(new JavaTimeModule()).build();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }


    @Test
    public void testCreatePatient() throws Exception {
        List<DoctorDTO> doctors = createDoctors();
        List<DoctorDTO> someDoctors = List.of(doctors.get(0), doctors.get(2));
        PatientDTO patient = createPatient(someDoctors);

        assertEquals(ADDRESS, patient.getAddress());
        assertEquals(AVATAR, patient.getAvatar());
        assertEquals(EMAIL, patient.getEmail());
        assertEquals(NAME, patient.getName());
        assertEquals(PHONE_NUMBER, patient.getPhoneNumber());
        assertEquals(SURNAME, patient.getSurname());
        assertEquals(BLOOD_GROUP, patient.getBloodGroup());
        assertEquals(CHRONIC_PATIENT, patient.getChronicPatient());
        assertEquals(IDP, patient.getIdp());
        assertEquals(NOTE, patient.getNotes());
        assertEquals(OPD, patient.getOpd());
        assertEquals(someDoctors.stream().map(DoctorDTO::getId).toList(), patient.getDoctorIds());
    }

    private List<DoctorDTO> createDoctors() throws Exception {
        DoctorDTO doctor = DoctorTest.createDoctor(DoctorTest.newDoctor(), objectMapper, mockMvc);
        DoctorDTO doctor2 = DoctorTest.createDoctor(DoctorTest.newDoctor2(), objectMapper, mockMvc);
        DoctorDTO doctor3 = DoctorTest.createDoctor(DoctorTest.newDoctor3(), objectMapper, mockMvc);
        return List.of(doctor, doctor2, doctor3);
    }

    @Test
    public void testGetPatients() throws Exception {
        List<DoctorDTO> doctors = createDoctors();
        PatientDTO patient = createPatient(newPatient(doctors));
        PatientDTO patient2 = createPatient(newPatient2());

        PatientFilterDTO filter = new PatientFilterDTO();
        int page = 0;
        int size = 10;
        String sort = "surname,desc";

        List<PatientDTO> patients = getPatients(page, size, sort, filter);
        assertEquals(2, patients.size());
        assertEquals(patients.get(0).getSurname(), SURNAME);
        assertEquals(patients.get(1).getSurname(), SURNAME2);

        patients = getPatients(page, size, "surname,asc", filter);
        assertEquals(2, patients.size());
        assertEquals(patients.get(0).getSurname(), SURNAME2);
        assertEquals(patients.get(1).getSurname(), SURNAME);

        patients = getPatients(page, 1, sort, filter);
        assertEquals(1, patients.size());
        assertEquals(patients.get(0).getSurname(), SURNAME);

        patients = getPatients(1, size, sort, filter);
        assertEquals(0, patients.size());

        filter = new PatientFilterDTO().idp(IDP);
        patients = getPatients(page, size, sort, filter);
        assertEquals(1, patients.size());
        assertEquals(patients.get(0).getId(), patient.getId());

        filter = new PatientFilterDTO().opd(OPD2);
        patients = getPatients(page, size, sort, filter);
        assertEquals(1, patients.size());
        assertEquals(patients.get(0).getId(), patient2.getId());

        filter = new PatientFilterDTO().doctorId(doctors.get(0).getId());
        patients = getPatients(page, size, sort, filter);
        assertEquals(1, patients.size());
        assertEquals(patients.get(0).getId(), patient.getId());

        filter = new PatientFilterDTO().text(SURNAME2);
        patients = getPatients(page, size, sort, filter);
        assertEquals(1, patients.size());
        assertEquals(patients.get(0).getId(), patient2.getId());
    }

    private List<PatientDTO> getPatients(int page, int size, String sort, PatientFilterDTO filter) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/api/patient/filter")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .contentType(MediaType.APPLICATION_JSON)
                .content(filter != null? objectMapper.writeValueAsString(filter): "")).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        List<PatientDTO> patients = objectMapper.readValue(response.getContentAsString(), new TypeReference<List<PatientDTO>>(){});
        return patients;
    }

    @Test
    public void testUpdatePatient() throws Exception {
        List<DoctorDTO> doctors = createDoctors();
        List<DoctorDTO> someDoctors = List.of(doctors.get(0), doctors.get(2));
        PatientDTO patient = createPatient(someDoctors);

        patient.setAddress(ADDRESS2);
        patient.setAvatar(AVATAR2);
        patient.setEmail(EMAIL2);
        patient.setName(NAME2);
        patient.setPhoneNumber(PHONE_NUMBER2);
        patient.setSurname(SURNAME2);
        patient.setBloodGroup(BLOOD_GROUP2);
        patient.setChronicPatient(CHRONIC_PATIENT2);
        patient.setIdp(IDP2);
        patient.setNotes(NOTE2);
        patient.setOpd(OPD2);
        List<Long> doctorIds = List.of(doctors.get(1).getId());
        patient.setDoctorIds(doctorIds);

        Long id = patient.getId();
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/api/patient/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient))).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        String content = response.getContentAsString();
        assertEquals("", content);

        patient = getPatient(id);

        assertEquals(id, patient.getId());
        assertEquals(ADDRESS2, patient.getAddress());
        assertEquals(AVATAR2, patient.getAvatar());
        assertEquals(EMAIL2, patient.getEmail());
        assertEquals(NAME2, patient.getName());
        assertEquals(PHONE_NUMBER2, patient.getPhoneNumber());
        assertEquals(SURNAME2, patient.getSurname());
        assertEquals(BLOOD_GROUP2, patient.getBloodGroup());
        assertEquals(CHRONIC_PATIENT2, patient.getChronicPatient());
        assertEquals(IDP2, patient.getIdp());
        assertEquals(NOTE2, patient.getNotes());
        assertEquals(OPD2, patient.getOpd());
        assertEquals(doctorIds, patient.getDoctorIds());
    }

    private PatientDTO createPatient (List<DoctorDTO> doctors) throws Exception {
        PatientDTO patient = newPatient(doctors);
        return createPatient(patient);
    }

    @NotNull
    public static PatientDTO newPatient(List<DoctorDTO> doctors) {
        PatientDTO patient = new PatientDTO();
        patient.setAddress(ADDRESS);
        patient.setAvatar(AVATAR);
        patient.setEmail(EMAIL);
        patient.setName(NAME);
        patient.setPhoneNumber(PHONE_NUMBER);
        patient.setSurname(SURNAME);
        patient.setBloodGroup(BLOOD_GROUP);
        patient.setChronicPatient(CHRONIC_PATIENT);
        patient.setIdp(IDP);
        patient.setNotes(NOTE);
        patient.setOpd(OPD);
        patient.setDoctorIds(doctors.stream().map(DoctorDTO::getId).toList());
        return patient;
    }

    @NotNull
    public static PatientDTO newPatient2() {
        PatientDTO patient = new PatientDTO();
        patient.setAddress(ADDRESS2);
        patient.setAvatar(AVATAR2);
        patient.setEmail(EMAIL2);
        patient.setName(NAME2);
        patient.setPhoneNumber(PHONE_NUMBER2);
        patient.setSurname(SURNAME2);
        patient.setBloodGroup(BLOOD_GROUP2);
        patient.setChronicPatient(CHRONIC_PATIENT2);
        patient.setIdp(IDP2);
        patient.setNotes(NOTE2);
        patient.setOpd(OPD2);
        return patient;
    }

    private PatientDTO createPatient(PatientDTO patient) throws Exception {
        return createPatient(patient, objectMapper, mockMvc);
    }

    public static PatientDTO createPatient(PatientDTO patient, ObjectMapper objectMapper, MockMvc mockMvc) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/api/patient")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(patient))).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        return objectMapper.readValue(response.getContentAsString(), PatientDTO.class);
    }


    @Test
    public void testGetPatient() throws Exception {
        List<DoctorDTO> doctors = createDoctors();
        List<DoctorDTO> someDoctors = List.of(doctors.get(0), doctors.get(2));
        PatientDTO patient = createPatient(someDoctors);
        patient = getPatient(patient.getId());

        assertEquals(ADDRESS, patient.getAddress());
        assertEquals(AVATAR, patient.getAvatar());
        assertEquals(EMAIL, patient.getEmail());
        assertEquals(NAME, patient.getName());
        assertEquals(PHONE_NUMBER, patient.getPhoneNumber());
        assertEquals(SURNAME, patient.getSurname());
        assertEquals(BLOOD_GROUP, patient.getBloodGroup());
        assertEquals(CHRONIC_PATIENT, patient.getChronicPatient());
        assertEquals(IDP, patient.getIdp());
        assertEquals(NOTE, patient.getNotes());
        assertEquals(OPD, patient.getOpd());
        assertEquals(someDoctors.stream().map(DoctorDTO::getId).toList(), patient.getDoctorIds());
    }

    @Test
    public void testDeletePatient() throws Exception {
        PatientDTO patient = createPatient(List.of());

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/patient/" + patient.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/patient/" + patient.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/patient/" + patient.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 404);
    }

    private PatientDTO getPatient(Long id) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/patient/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        PatientDTO patient = objectMapper.readValue(response.getContentAsString(), PatientDTO.class);
        return patient;
    }
}
