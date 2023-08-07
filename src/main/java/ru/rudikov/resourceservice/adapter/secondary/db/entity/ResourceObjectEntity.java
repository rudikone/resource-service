package ru.rudikov.resourceservice.adapter.secondary.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObjectEntity {

    @Id
    private Integer id;
    private String value;
    private String path;
}
