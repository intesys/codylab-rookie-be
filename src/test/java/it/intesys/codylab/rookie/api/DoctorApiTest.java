package it.intesys.codylab.rookie.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.intesys.codylab.rookie.dto.DoctorDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class DoctorApiTest {
    private MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void createDoctorTest () throws Exception {
        DoctorDTO requestDTO = new DoctorDTO();
        requestDTO.setName("Matteo");
        requestDTO.setSurname("Ocneanu");
        requestDTO.setEmail("matteo.ocneanu@gmail.com");
        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(DoctorApi.API_DOCTOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        DoctorDTO responseDTO = jsonMapper.readValue(jsonResponse, DoctorDTO.class);
        Assertions.assertNotNull(responseDTO.getID(), "doctor id");
        Assertions.assertEquals(requestDTO.getName(), responseDTO.getName(), "doctor name");
    }

    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}
