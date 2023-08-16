package ru.rudikov.resourceservice.adapter.secondary.db.resource;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.ResourceObjectEntity;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.secondary.ResourceObjectPort;

@Service
@RequiredArgsConstructor
public class ResourceObjectAdapter implements ResourceObjectPort {

    private final ResourceObjectRepository repository;

    @Override
    public Mono<Integer> save(ResourceObject resourceObject) {
        val entity = new ResourceObjectEntity(
                resourceObject.getId(),
                resourceObject.getValue(),
                resourceObject.getPath()
        );

        return repository.save(entity).map(ResourceObjectEntity::getId);
    }

    @Override
    public Mono<ResourceObject> get(int id) {
        return repository.findById(id)
                .map(entity -> new ResourceObject(entity.getId(), entity.getValue(), entity.getPath()));
    }
}
