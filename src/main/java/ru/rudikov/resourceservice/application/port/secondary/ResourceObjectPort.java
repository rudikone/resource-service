package ru.rudikov.resourceservice.application.port.secondary;

import ru.rudikov.resourceservice.application.domain.model.dto.ResourceObject;

public interface ResourceObjectPort {

    public Integer save(ResourceObject resourceObject);

    public ResourceObject get(int id);
}
