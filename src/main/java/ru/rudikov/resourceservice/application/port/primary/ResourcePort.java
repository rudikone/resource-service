package ru.rudikov.resourceservice.application.port.primary;

import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;

public interface ResourcePort {

  Mono<Integer> save(ResourceObject object);

  Mono<ResourceObject> get(Integer id);
}
