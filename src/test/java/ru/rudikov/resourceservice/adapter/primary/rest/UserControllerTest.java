package ru.rudikov.resourceservice.adapter.primary.rest;

import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.port.primary.UserInfoPort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;
import ru.rudikov.resourceservice.configuration.WebSecurityConfig;

@WebFluxTest(controllers = UserController.class)
@Import({
  WebSecurityConfig.class,
  JwtService.class,
})
public class UserControllerTest {

  @Autowired private WebTestClient webTestClient;

  @Autowired private ObjectMapper objectMapper;

  @MockBean private UserInfoPort port;

  private final String USER = "USER";
  private final String ADMIN = "ADMIN";

  private final String usersUri = "/users";

  @Nested
  class GetAllUsersControllerTest {

    @Test
    @WithMockUser(authorities = USER)
    public void Should_Return_isOk_For_User() {
      webTestClient.get().uri(usersUri).exchange().expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_Ok_For_Admin() {
      webTestClient.get().uri(usersUri).exchange().expectStatus().isOk();
    }

    @Test
    public void Should_Return_isUnauthorized_For_Anonymous() {
      webTestClient.get().uri(usersUri).exchange().expectStatus().isUnauthorized();
    }
  }

  @Nested
  class GetUserByIdControllerTest {

    @Test
    @WithMockUser(authorities = USER)
    public void Should_Return_isOk_For_User() {
      String userId = "123";
      var user = Instancio.create(UserDto.class);

      given(port.getUserById(userId)).willReturn(Mono.just(user));

      webTestClient.get().uri(usersUri + "/{userId}", userId).exchange().expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_Ok_For_Admin() {
      String userId = "123";
      var user = Instancio.create(UserDto.class);

      given(port.getUserById(userId)).willReturn(Mono.just(user));

      webTestClient.get().uri(usersUri + "/{userId}", userId).exchange().expectStatus().isOk();
    }

    @Test
    public void Should_Return_isUnauthorized_For_Anonymous() {
      String userId = "123";
      var user = Instancio.create(UserDto.class);

      given(port.getUserById(userId)).willReturn(Mono.just(user));

      webTestClient
          .get()
          .uri(usersUri + "/{userId}", userId)
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_NotFound_When_Port_Returns_Empty_Mono() {
      String userId = "123";
      given(port.getUserById(userId)).willReturn(Mono.empty());

      webTestClient
          .get()
          .uri(usersUri + "/{userId}", userId)
          .exchange()
          .expectStatus()
          .isNotFound();
    }
  }

  @Nested
  class SearchUsersByNameControllerTest {

    @Test
    @WithMockUser(authorities = USER)
    public void Should_Return_isOk_For_User() {
      String name = "name";
      var user = Instancio.create(UserDto.class);

      given(port.searchUsers(name)).willReturn(Flux.just(user));

      webTestClient
          .get()
          .uri(usersUri + "/search?name={name}", name)
          .exchange()
          .expectStatus()
          .isOk();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_Ok_For_Admin() {
      String name = "name";
      var user = Instancio.create(UserDto.class);

      given(port.searchUsers(name)).willReturn(Flux.just(user));

      webTestClient
          .get()
          .uri(usersUri + "/search?name={name}", name)
          .exchange()
          .expectStatus()
          .isOk();
    }

    @Test
    public void Should_Return_isUnauthorized_For_Anonymous() {
      String name = "name";
      var user = Instancio.create(UserDto.class);

      given(port.searchUsers(name)).willReturn(Flux.just(user));

      webTestClient
          .get()
          .uri(usersUri + "/search?name={name}", name)
          .exchange()
          .expectStatus()
          .isUnauthorized();
    }
  }

  @Nested
  class StreamAllUsersControllerTest {

    @Test
    @WithMockUser(authorities = USER)
    public void Should_Return_Ok_For_User() {
      var user = Instancio.create(UserDto.class);

      given(port.streamAllUsers()).willReturn(Flux.just(user));

      webTestClient.get().uri(usersUri + "/stream").exchange().expectStatus().isOk();
    }

    @Test
    @WithMockUser(authorities = ADMIN)
    public void Should_Return_Ok_For_Admin() {
      var user = Instancio.create(UserDto.class);

      given(port.streamAllUsers()).willReturn(Flux.just(user));

      webTestClient.get().uri(usersUri + "/stream").exchange().expectStatus().isOk();
    }

    @Test
    public void Should_Return_isUnauthorized_For_Anonymous() {
      var user = Instancio.create(UserDto.class);

      given(port.streamAllUsers()).willReturn(Flux.just(user));

      webTestClient.get().uri(usersUri + "/stream").exchange().expectStatus().isUnauthorized();
    }
  }
}
