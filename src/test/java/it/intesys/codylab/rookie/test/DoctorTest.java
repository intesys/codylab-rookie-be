package it.intesys.codylab.rookie.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.codylab.rookie.App;
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

import javax.print.Doc;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class DoctorTest {
    public static final String ADDRESS = "Via Roveggia";
    public static final String AVATAR = "base64 image";
    public static final String EMAIL = "carlo.marchiori@intesys.it";
    public static final String NAME = "Carlo";
    public static final String PHONE_NUMBER = "6666666666";
    public static final String PROFESSION = "IT";
    public static final String SURNAME = "Marchiori";

    public static final String ADDRESS2 = "Via Albere";
    public static final String AVATAR2 = "base64 image 2";
    public static final String EMAIL2 = "enrico.costanzi@intesys.it";
    public static final String NAME2 = "Enrico";
    public static final String PHONE_NUMBER2 = "7777777777";
    public static final String PROFESSION2 = "IT Architect";
    public static final String SURNAME2 = "Costanzi";
    @Autowired
    private WebApplicationContext applicationContext;
    private  ObjectMapper objectMapper = new ObjectMapper();

    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext).build();
    }


    @Test
    public void testCreateDoctor() throws Exception {
        DoctorDTO doctor = createDoctor();

        assertEquals(ADDRESS, doctor.getAddress());
        assertEquals(AVATAR, doctor.getAvatar());
        assertEquals(EMAIL, doctor.getEmail());
        assertEquals(NAME, doctor.getName());
        assertEquals(PHONE_NUMBER, doctor.getPhoneNumber());
        assertEquals(PROFESSION, doctor.getProfession());
        assertEquals(SURNAME, doctor.getSurname());
    }

    @Test
    public void testUpdateDoctor() throws Exception {
        DoctorDTO doctor = createDoctor();

        doctor.setAddress(ADDRESS2);
        doctor.setAvatar(AVATAR2);
        doctor.setEmail(EMAIL2);
        doctor.setName(NAME2);
        doctor.setPhoneNumber(PHONE_NUMBER2);
        doctor.setProfession(PROFESSION2);
        doctor.setSurname(SURNAME2);

        Long id = doctor.getId();
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.put("/api/doctor/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor))).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        String content = response.getContentAsString();
        assertEquals("", content);

        doctor = getDoctor(id);

        assertEquals(id, doctor.getId());
        assertEquals(ADDRESS2, doctor.getAddress());
        assertEquals(AVATAR2, doctor.getAvatar());
        assertEquals(EMAIL2, doctor.getEmail());
        assertEquals(NAME2, doctor.getName());
        assertEquals(PHONE_NUMBER2, doctor.getPhoneNumber());
        assertEquals(PROFESSION2, doctor.getProfession());
        assertEquals(SURNAME2, doctor.getSurname());
    }

    private DoctorDTO createDoctor () throws Exception {
        DoctorDTO doctor = new DoctorDTO();
        doctor.setAddress(ADDRESS);
        doctor.setAvatar(AVATAR);
        doctor.setEmail(EMAIL);
        doctor.setName(NAME);
        doctor.setPhoneNumber(PHONE_NUMBER);
        doctor.setProfession(PROFESSION);
        doctor.setSurname(SURNAME);

        return createDoctor(doctor);
    }

    private DoctorDTO createDoctor(DoctorDTO doctor) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.post("/api/doctor")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(doctor))).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        return objectMapper.readValue(response.getContentAsString(), DoctorDTO.class);
    }


    @Test
    public void testGetDoctor() throws Exception {
        DoctorDTO doctor = createDoctor();
        doctor = getDoctor(doctor.getId());

        assertEquals(doctor.getAddress(), ADDRESS);
        assertEquals(doctor.getAvatar(), AVATAR);
        assertEquals(doctor.getEmail(), EMAIL);
        assertEquals(doctor.getName(), NAME);
        assertEquals(doctor.getPhoneNumber(), PHONE_NUMBER);
        assertEquals(doctor.getProfession(), PROFESSION);
        assertEquals(doctor.getSurname(), SURNAME);
    }

    @Test
    public void testDeleteDoctor() throws Exception {
        DoctorDTO doctor = createDoctor();

        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/doctor/" + doctor.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/doctor/" + doctor.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        response = mockMvc.perform(MockMvcRequestBuilders.get("/api/doctor/" + doctor.getId())
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 404);

        assertEquals(doctor.getAddress(), ADDRESS);
        assertEquals(doctor.getAvatar(), AVATAR);
        assertEquals(doctor.getEmail(), EMAIL);
        assertEquals(doctor.getName(), NAME);
        assertEquals(doctor.getPhoneNumber(), PHONE_NUMBER);
        assertEquals(doctor.getProfession(), PROFESSION);
        assertEquals(doctor.getSurname(), SURNAME);
    }

    private DoctorDTO getDoctor(Long id) throws Exception {
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders.get("/api/doctor/" + id)
                .contentType(MediaType.APPLICATION_JSON)).andReturn().getResponse();
        assertEquals(response.getStatus(), 200);

        DoctorDTO doctor = objectMapper.readValue(response.getContentAsString(), DoctorDTO.class);
        return doctor;
    }
}
