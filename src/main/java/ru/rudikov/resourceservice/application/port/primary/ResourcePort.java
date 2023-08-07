package ru.rudikov.resourceservice.application.port.primary;

import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;

public interface ResourcePort {

    public Integer save(ResourceObject object);

    public ResourceObject get(Integer id);
}
