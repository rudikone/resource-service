package ru.rudikov.resourceservice.application.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.primary.ResourcePort;
import ru.rudikov.resourceservice.application.port.secondary.ResourceObjectPort;

@Service
@RequiredArgsConstructor
public class ResourceUseCase implements ResourcePort {

  private final ResourceObjectPort port;

  @Override
  public Mono<Integer> save(ResourceObject object) {
    return port.save(object);
  }

  @Override
  public Mono<ResourceObject> get(Integer id) {
    return port.get(id);
  }
}
