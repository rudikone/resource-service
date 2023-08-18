package ru.rudikov.resourceservice.application.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

  private String id;
  private String name;
  private int age;
  private double salary;
  private String department;
}
