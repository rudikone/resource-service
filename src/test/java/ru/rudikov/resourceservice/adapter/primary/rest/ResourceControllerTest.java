package ru.rudikov.resourceservice.adapter.primary.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class GetResourceControllerTest {

        @Test
        public void Should_Return_Ok_For_User() throws Exception {
            mockMvc.perform(get("/resource/1")
                    .with(httpBasic("user", "password"))
            ).andExpect(status().isOk());
        }

        @Test
        public void Should_Return_Ok_For_Admin() throws Exception {
            mockMvc.perform(get("/resource/1")
                    .with(httpBasic("admin", "admin"))
            ).andExpect(status().isOk());
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() throws Exception {
            mockMvc.perform(get("/resource"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class PostResourceControllerTest {

        @Test
        public void Should_Return_isForbidden_For_User() throws Exception {
            var resourceObject = new ResourceObject(1, "value", "path");

            mockMvc.perform(post("/resource")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resourceObject))
                    .with(httpBasic("user", "password"))
            ).andExpect(status().isForbidden());
        }

        @Test
        public void Should_Return_Ok_For_Admin() throws Exception {
            var resourceObject = new ResourceObject(1, "value", "path");

            mockMvc.perform(post("/resource")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resourceObject))
                    .with(httpBasic("admin", "admin"))
            ).andExpect(status().isOk());
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() throws Exception {
            var resourceObject = new ResourceObject(1, "value", "path");

            mockMvc.perform(post("/resource")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resourceObject))
            ).andExpect(status().isUnauthorized());
        }
    }
}