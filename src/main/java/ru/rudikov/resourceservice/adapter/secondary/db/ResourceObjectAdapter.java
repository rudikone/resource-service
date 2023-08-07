package ru.rudikov.resourceservice.adapter.secondary.db;

import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.ResourceObjectEntity;
import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;
import ru.rudikov.resourceservice.application.port.secondary.ResourceObjectPort;

@Service
@RequiredArgsConstructor
public class ResourceObjectAdapter implements ResourceObjectPort {

    private final ResourceObjectRepository repository;


    @Override
    public Integer save(ResourceObject resourceObject) {
        val entity = new ResourceObjectEntity(
                resourceObject.getId(),
                resourceObject.getValue(),
                resourceObject.getPath()
        );

        return repository.save(entity).getId();
    }


    @Override
    public ResourceObject get(int id) {
        return repository.findById(id)
                .map(entity -> new ResourceObject(entity.getId(), entity.getValue(), entity.getPath()))
                .orElse(null);
    }
}
