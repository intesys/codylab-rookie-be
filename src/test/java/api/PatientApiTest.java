package api;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import rookie.dto.PatientDTO;

@SpringBootTest
public class PatientApiTest {
    private MockMvc mvc;

    @Autowired
    WebApplicationContext context;

    ObjectMapper jsonMapper = new ObjectMapper();

    @Test
    void createPatientTest() throws Exception {
        PatientDTO requestDTO = new PatientDTO();
        requestDTO.setName("Ale");
        requestDTO.setSurname("baltieri");
        requestDTO.setEmail("alebaltieri@gmail.com");

        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.post("/api/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        PatientDTO responseDTO = jsonMapper.readValue(jsonResponse, PatientDTO.class);

        Assertions.assertNotNull(responseDTO.getId(), "patient id");
        Assertions.assertEquals(requestDTO.getName(), responseDTO.getName(), "patient name");
    }

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }
}
