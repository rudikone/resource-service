package ru.rudikov.resourceservice.adapter.primary.rest;

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
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;
import ru.rudikov.resourceservice.application.port.primary.UserManagementPort;
import ru.rudikov.resourceservice.application.service.auth.JwtService;
import ru.rudikov.resourceservice.configuration.WebSecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@WebFluxTest(controllers = UserController.class)
@Import({WebSecurityConfig.class, JwtService.class,})
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserManagementPort userManagementPort;

    private final String USER = "USER";
    private final String ADMIN = "ADMIN";

    private final String usersUri = "/users";

    @Nested
    class CreateUserControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_isForbidden_For_User() throws Exception {
            var user = Instancio.create(UserManagementDto.class);

            webTestClient.post()
                    .uri(usersUri)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isForbidden();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() throws Exception {
            var user = Instancio.create(UserManagementDto.class);

            webTestClient.post()
                    .uri(usersUri)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isCreated();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() throws Exception {
            var user = Instancio.create(UserManagementDto.class);

            webTestClient.post()
                    .uri(usersUri)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isUnauthorized();
        }
    }

    @Nested
    class GetAllUsersControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_Ok_For_User() {
            webTestClient.get()
                    .uri(usersUri)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() {
            webTestClient.get()
                    .uri(usersUri)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() {
            webTestClient.get()
                    .uri(usersUri)
                    .exchange()
                    .expectStatus().isUnauthorized();
        }
    }

    @Nested
    class GetUserByIdControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_Ok_For_User() {
            String userId = "123";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.getUserById(userId)).willReturn(Mono.just(user));

            webTestClient.get()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() {
            String userId = "123";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.getUserById(userId)).willReturn(Mono.just(user));

            webTestClient.get()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() {
            String userId = "123";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.getUserById(userId)).willReturn(Mono.just(user));

            webTestClient.get()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isUnauthorized();
        }

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_NotFound_When_Port_Returns_Empty_Mono() {
            String userId = "123";
            given(userManagementPort.getUserById(userId)).willReturn(Mono.empty());

            webTestClient.get()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    class UpdateUserControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_isForbidden_For_User() throws Exception {
            String userId = "123";
            var user = Instancio.create(UserManagementDto.class);

            webTestClient.put()
                    .uri(usersUri + "/{userId}", userId)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isForbidden();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() throws Exception {
            String userId = "123";
            var user = Instancio.create(UserManagementDto.class);

            given(userManagementPort.updateUserById(any(), any())).willReturn(Mono.just(user));

            webTestClient.put()
                    .uri(usersUri + "/{userId}", userId)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() throws Exception {
            String userId = "123";
            var user = Instancio.create(UserManagementDto.class);

            webTestClient.put()
                    .uri(usersUri + "/{userId}", userId)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isUnauthorized();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_BadRequest_When_Port_Returns_Empty_Mono() throws Exception {
            String userId = "123";
            var user = Instancio.create(UserManagementDto.class);

            given(userManagementPort.updateUserById(any(), any())).willReturn(Mono.empty());

            webTestClient.put()
                    .uri(usersUri + "/{userId}", userId)
                    .contentType(APPLICATION_JSON)
                    .bodyValue(objectMapper.writeValueAsString(user))
                    .exchange()
                    .expectStatus().isBadRequest();
        }
    }

    @Nested
    class DeleteUserByIdControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_isForbidden_For_User() {
            String userId = "123";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.deleteUserById(userId)).willReturn(Mono.just(user));

            webTestClient.delete()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isForbidden();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() {
            String userId = "123";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.deleteUserById(userId)).willReturn(Mono.just(user));

            webTestClient.delete()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() {
            String userId = "123";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.deleteUserById(userId)).willReturn(Mono.just(user));

            webTestClient.delete()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isUnauthorized();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_NotFound_When_Port_Returns_Empty_Mono() {
            String userId = "123";

            given(userManagementPort.deleteUserById(userId)).willReturn(Mono.empty());

            webTestClient.delete()
                    .uri(usersUri + "/{userId}", userId)
                    .exchange()
                    .expectStatus().isNotFound();
        }
    }

    @Nested
    class SearchUsersByNameControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_Ok_For_User() {
            String name = "name";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.searchUsers(name)).willReturn(Flux.just(user));

            webTestClient.get()
                    .uri(usersUri + "/search?name={name}", name)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() {
            String name = "name";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.searchUsers(name)).willReturn(Flux.just(user));

            webTestClient.get()
                    .uri(usersUri + "/search?name={name}", name)
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() {
            String name = "name";
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.searchUsers(name)).willReturn(Flux.just(user));

            webTestClient.get()
                    .uri(usersUri + "/search?name={name}", name)
                    .exchange()
                    .expectStatus().isUnauthorized();
        }
    }

    @Nested
    class StreamAllUsersControllerTest {

        @Test
        @WithMockUser(authorities = USER)
        public void Should_Return_Ok_For_User() {
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.streamAllUsers()).willReturn(Flux.just(user));

            webTestClient.get()
                    .uri(usersUri + "/stream")
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        @WithMockUser(authorities = ADMIN)
        public void Should_Return_Ok_For_Admin() {
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.streamAllUsers()).willReturn(Flux.just(user));

            webTestClient.get()
                    .uri(usersUri + "/stream")
                    .exchange()
                    .expectStatus().isOk();
        }

        @Test
        public void Should_Return_isUnauthorized_For_Anonymous() {
            var user = Instancio.create(UserDto.class);

            given(userManagementPort.streamAllUsers()).willReturn(Flux.just(user));

            webTestClient.get()
                    .uri(usersUri + "/stream")
                    .exchange()
                    .expectStatus().isUnauthorized();
        }
    }
}
