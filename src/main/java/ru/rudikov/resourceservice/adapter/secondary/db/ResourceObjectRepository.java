package ru.rudikov.resourceservice.adapter.secondary.db;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.ResourceObjectEntity;

public interface ResourceObjectRepository extends ReactiveMongoRepository<ResourceObjectEntity, Integer> {
}
