package com.github.vahundos.breezer.web.controller;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.vahundos.breezer.TestConstants;
import com.github.vahundos.breezer.web.IntegrationTest;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Map;

import static com.github.vahundos.breezer.TestData.PASSWORD;
import static com.github.vahundos.breezer.TestData.USERNAME;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@IntegrationTest
@Sql(scripts = "/init-data.sql")
public class AbstractControllerIT {

    protected final ObjectMapper objectMapper = new ObjectMapper().setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Autowired
    protected MockMvc mockMvc;

    @SneakyThrows
    protected String getAuthToken() {
        MvcResult mvcResult = this.mockMvc.perform(post(TestConstants.AUTHENTICATE_PATH).with(httpBasic(USERNAME, PASSWORD)))
                                          .andExpect(status().isOk())
                                          .andReturn();

        Map<String, String> responseMap = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                                                                 new TypeReference<>() {
                                                                 });
        return responseMap.get(AuthenticationRestController.AUTH_TOKEN);
    }
}
