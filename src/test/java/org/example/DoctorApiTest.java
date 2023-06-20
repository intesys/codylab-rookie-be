package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.api.DoctorApi;
import org.example.dto.DoctorDTO;
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
    void createDoctorTest () throws Exception{
        DoctorDTO requestDTO = new DoctorDTO();
        requestDTO.setName("Carlo");
        requestDTO.setSurname("Marchiori");
        requestDTO.setEmail("carlo.marchiori@gmail.com");
        String jsonRequest = jsonMapper.writeValueAsString(requestDTO);

        MvcResult result = mvc.perform(MockMvcRequestBuilders
                        .post(DoctorApi.API_DOCTOR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        DoctorDTO responseDTO = jsonMapper.readValue(jsonResponse, DoctorDTO.class);
        Assertions.assertNotNull(responseDTO.getId(), "doctor id");
        Assertions.assertEquals(requestDTO.getName(), responseDTO.getName(), "doctor name");
    }

    @BeforeEach
    void setup () {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

}
