package ru.rudikov.resourceservice.application.service.transform;

import org.mapstruct.Mapper;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.User;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.Role;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserManagementDto userDto);

    UserManagementDto toUserManagementDto(User user);

    UserDto toUserDto(User user);

    Set<String> mapRoles(Set<Role> roles);

    default Role mapRole(String role) {
        return Role.valueOf(role);
    }

    default String mapRole(Role role) {
        return role.name();
    }
}
