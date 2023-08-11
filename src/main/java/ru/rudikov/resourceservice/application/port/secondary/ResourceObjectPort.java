package ru.rudikov.resourceservice.application.port.secondary;

import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;

public interface ResourceObjectPort {

    Mono<Integer> save(ResourceObject resourceObject);

    Mono<ResourceObject> get(int id);
}
