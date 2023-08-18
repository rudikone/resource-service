package ru.rudikov.resourceservice.adapter.primary.rest;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.port.primary.UserInfoPort;

@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/users")
public class UserController {

  private final UserInfoPort port;

  @GetMapping
  public Flux<UserDto> getAllUsers() {
    return port.getAllUsers();
  }

  @GetMapping("/{userId}")
  public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable String userId) {
    Mono<UserDto> user = port.getUserById(userId);
    return user.map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/search")
  public Flux<UserDto> searchUsers(@RequestParam("name") String name) {
    return port.searchUsers(name);
  }

  @GetMapping(value = "/stream", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<UserDto> streamAllUsers() {
    return port.streamAllUsers();
  }
}
