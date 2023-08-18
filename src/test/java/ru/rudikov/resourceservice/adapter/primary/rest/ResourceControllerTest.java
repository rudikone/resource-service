package ru.rudikov.resourceservice.adapter.primary.rest;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.primary.ResourcePort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;
import ru.rudikov.resourceservice.configuration.WebSecurityConfig;

@WebFluxTest(controllers = ResourceController.class)
@MockBeans(@MockBean(ResourcePort.class))
@Import({
  WebSecurityConfig.class,
  JwtService.class,
})
public class ResourceControllerTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private ObjectMapper objectMapper;

  private static final String USER = "USER";
  private static final String ADMIN = "ADMIN";

  @Nested
  class GetResourceControllerTest {

    @Test
    @WithMockUser(authorities = USER)
    public void Should_Return_Ok_For_User() {
      webTestClient.get().uri("/resource/1").exchange().expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_Ok_For_Admin() {
      webTestClient.get().uri("/resource/1").exchange().expectStatus().isOk();
    }

    @Test
    public void Should_Return_isUnauthorized_For_Anonymous() {
      webTestClient.get().uri("/resource/1").exchange().expectStatus().isUnauthorized();
    }
  }

  @Nested
  class PostResourceControllerTest {

    @Test
    @WithMockUser(authorities = USER)
    public void Should_Return_isForbidden_For_User() throws Exception {
      var resourceObject = new ResourceObject(1, "value", "path");

      webTestClient
          .post()
          .uri("/resource")
          .contentType(APPLICATION_JSON)
          .bodyValue(objectMapper.writeValueAsString(resourceObject))
          .exchange()
          .expectStatus()
          .isForbidden();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_Ok_For_Admin() throws Exception {
      var resourceObject = new ResourceObject(1, "value", "path");

      webTestClient
          .post()
          .uri("/resource")
          .contentType(APPLICATION_JSON)
          .bodyValue(objectMapper.writeValueAsString(resourceObject))
          .exchange()
          .expectStatus()
          .isOk();
    }

    @Test
    public void Should_Return_isUnauthorized_For_Anonymous() throws Exception {
      var resourceObject = new ResourceObject(1, "value", "path");

      webTestClient
          .post()
          .uri("/resource")
          .contentType(APPLICATION_JSON)
          .bodyValue(objectMapper.writeValueAsString(resourceObject))
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }
  }
}
