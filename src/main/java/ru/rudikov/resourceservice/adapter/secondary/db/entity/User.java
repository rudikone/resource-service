package ru.rudikov.resourceservice.adapter.secondary.db.entity;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@ToString
@EqualsAndHashCode(of = {"id", "name", "department"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(value = "users")
public class User {

  @Id private String id;
  private String name;
  private int age;
  private double salary;
  private String department;
  private String login;
  private String password;
  private Set<String> roles;
}
