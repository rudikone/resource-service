package ru.rudikov.resourceservice.adapter.secondary.db;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.ResourceObjectEntity;

public interface ResourceObjectRepository extends JpaRepository<ResourceObjectEntity, Integer> {
}
