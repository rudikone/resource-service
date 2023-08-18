package ru.rudikov.resourceservice.adapter.primary.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.primary.ResourcePort;

@RestController
@RequiredArgsConstructor
@RequestMapping("/resource")
public class ResourceController {

  private final ResourcePort port;

  @PostMapping
  public Mono<Integer> createResourceObject(@RequestBody ResourceObject object) {
    return port.save(object);
  }

  @GetMapping("/{id}")
  public Mono<ResourceObject> getResourceObject(@PathVariable Integer id) {
    return port.get(id);
  }
}
