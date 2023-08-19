package ru.rudikov.resourceservice.application.service;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import org.instancio.Instancio;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;

class UserManagementUseCaseTest {

  private final UserPort userPort = Mockito.mock();

  private final UserManagementUseCase userManagementUseCase = new UserManagementUseCase(userPort);

  @Test
  void Should_Search_Users() {
    String name = "John";
    UserManagementDto user1 = Instancio.create(UserManagementDto.class);
    UserManagementDto user2 = Instancio.create(UserManagementDto.class);
    UserManagementDto user3 = Instancio.create(UserManagementDto.class);
    List<UserManagementDto> userList = Arrays.asList(user1, user2, user3);
    Flux<UserManagementDto> expectedUsers = Flux.fromIterable(userList);
    when(userPort.searchUsers(name)).thenReturn(expectedUsers);

    Flux<UserManagementDto> actualUsers = userManagementUseCase.searchUsers(name);

    StepVerifier.create(actualUsers).expectNext(user1, user2, user3).verifyComplete();
    verify(userPort, times(1)).searchUsers(name);
  }

  @Test
  void Should_Delete_User_By_Id() {
    String userId = "123";
    UserManagementDto user = Instancio.create(UserManagementDto.class);
    when(userPort.deleteUserById(userId)).thenReturn(Mono.just(user));

    Mono<UserManagementDto> result = userManagementUseCase.deleteUserById(userId);

    StepVerifier.create(result).expectNext(user).verifyComplete();
    verify(userPort, times(1)).deleteUserById(userId);
  }

  @Test
  void Should_Update_User() {
    String id = "1";
    UserManagementDto user = Instancio.create(UserManagementDto.class);
    when(userPort.updateUserById(id, user)).thenReturn(Mono.just(user));

    Mono<UserManagementDto> result = userManagementUseCase.updateUserById(id, user);

    StepVerifier.create(result).expectNext(user).verifyComplete();
    verify(userPort, times(1)).updateUserById(id, user);
  }

  @Test
  void Should_Get_User_By_Id() {
    String userId = "123";
    UserManagementDto user = Instancio.create(UserManagementDto.class);
    when(userPort.getUserById(userId)).thenReturn(Mono.just(user));

    Mono<UserManagementDto> result = userManagementUseCase.getUserById(userId);

    StepVerifier.create(result).expectNext(user).verifyComplete();

    verify(userPort, times(1)).getUserById(userId);
  }

  @Test
  void Should_Get_All_Users() {
    UserManagementDto user1 = Instancio.create(UserManagementDto.class);
    UserManagementDto user2 = Instancio.create(UserManagementDto.class);
    when(userPort.getAllUsers()).thenReturn(Flux.just(user1, user2));

    Flux<UserManagementDto> result = userManagementUseCase.getAllUsers();

    StepVerifier.create(result).expectNext(user1, user2).verifyComplete();
    verify(userPort, times(1)).getAllUsers();
  }

  @Test
  void Should_Create_User() {
    String id = "1";
    UserManagementDto user = new UserManagementDto();
    user.setId(id);
    when(userPort.create(user)).thenReturn(Mono.just(id));

    Mono<String> result = userManagementUseCase.create(user);

    StepVerifier.create(result).expectNext(user.getId()).verifyComplete();
    verify(userPort, times(1)).create(user);
  }

  @Test
  @Disabled("Проверить случайный порядок и интервал")
  public void Should_Stream_All_Users() {
    UserManagementDto user1 = Instancio.create(UserManagementDto.class);
    UserManagementDto user2 = Instancio.create(UserManagementDto.class);
    UserManagementDto user3 = Instancio.create(UserManagementDto.class);
    Flux<UserManagementDto> userFlux = Flux.just(user1, user2, user3);
    when(userPort.getAllUsers()).thenReturn(userFlux);

    Flux<UserManagementDto> resultFlux = userManagementUseCase.streamAllUsers();

    StepVerifier.create(resultFlux)
        .expectNext(user1)
        .expectNext(user2)
        .expectNext(user3)
        .thenCancel()
        .verify();
  }
}
