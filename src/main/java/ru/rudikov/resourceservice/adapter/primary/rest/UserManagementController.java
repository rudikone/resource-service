package ru.rudikov.resourceservice.adapter.primary.rest;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.TEXT_EVENT_STREAM_VALUE;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;
import ru.rudikov.resourceservice.application.port.primary.UserManagementPort;

@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/management/users")
public class UserManagementController {

  private final UserManagementPort port;

  @PostMapping
  @ResponseStatus(CREATED)
  public Mono<String> create(@RequestBody UserManagementDto user) {
    return port.create(user);
  }

  @GetMapping
  public Flux<UserManagementDto> getAllUsers() {
    return port.getAllUsers();
  }

  @GetMapping("/{userId}")
  public Mono<ResponseEntity<UserManagementDto>> getUserById(@PathVariable String userId) {
    return port.getUserById(userId)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @PutMapping("/{userId}")
  public Mono<ResponseEntity<UserManagementDto>> updateUserById(
      @PathVariable String userId, @RequestBody UserManagementDto user) {
    return port.updateUserById(userId, user)
        .map(ResponseEntity::ok)
        .switchIfEmpty(Mono.just(ResponseEntity.badRequest().build()));
  }

  @DeleteMapping("/{userId}")
  public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable String userId) {
    return port.deleteUserById(userId)
        .map(r -> ResponseEntity.ok().<Void>build())
        .defaultIfEmpty(ResponseEntity.notFound().build());
  }

  @GetMapping("/search")
  public Flux<UserManagementDto> searchUsers(@RequestParam("name") String name) {
    return port.searchUsers(name);
  }

  @GetMapping(value = "/stream", produces = TEXT_EVENT_STREAM_VALUE)
  public Flux<UserManagementDto> streamAllUsers() {
    return port.streamAllUsers();
  }
}
