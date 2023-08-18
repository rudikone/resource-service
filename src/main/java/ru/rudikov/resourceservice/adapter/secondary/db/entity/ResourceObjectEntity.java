package ru.rudikov.resourceservice.adapter.secondary.db.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "resource_objects")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceObjectEntity {

  @Id private Integer id;
  private String value;
  private String path;
}
