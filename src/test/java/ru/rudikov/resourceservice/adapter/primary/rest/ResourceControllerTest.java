package ru.rudikov.resourceservice.adapter.primary.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.primary.ResourcePort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;
import ru.rudikov.resourceservice.configuration.WebSecurityConfig;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ResourceController.class)
@MockBeans(@MockBean(ResourcePort.class))
@Import({WebSecurityConfig.class, JwtService.class,})
public class ResourceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String USER = "USER";
    private static final String ADMIN = "ADMIN";

    @Nested
    class GetResourceControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_Ok_For_User() throws Exception {
            mockMvc.perform(get("/resource/1")).andExpect(status().isOk());
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() throws Exception {
            mockMvc.perform(get("/resource/1")).andExpect(status().isOk());
        }

        @Test
        public void Should_Return_isForbidden_For_Anonymous() throws Exception {
            mockMvc.perform(get("/resource")).andExpect(status().isForbidden());
        }
    }

    @Nested
    class PostResourceControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_isForbidden_For_User() throws Exception {
            var resourceObject = new ResourceObject(1, "value", "path");

            mockMvc.perform(post("/resource")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resourceObject))
            ).andExpect(status().isForbidden());
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() throws Exception {
            var resourceObject = new ResourceObject(1, "value", "path");

            mockMvc.perform(post("/resource")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resourceObject))
            ).andExpect(status().isOk());
        }

        @Test
        public void Should_Return_isForbidden_For_Anonymous() throws Exception {
            var resourceObject = new ResourceObject(1, "value", "path");

            mockMvc.perform(post("/resource")
                    .contentType(APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(resourceObject))
            ).andExpect(status().isForbidden());
        }
    }
}