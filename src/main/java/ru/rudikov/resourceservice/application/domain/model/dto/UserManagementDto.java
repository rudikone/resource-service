package ru.rudikov.resourceservice.application.domain.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.Role;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserManagementDto {

    private String id;
    private String name;
    private int age;
    private double salary;
    private String department;
    private String login;
    private String password;
    private Set<Role> roles;
}
