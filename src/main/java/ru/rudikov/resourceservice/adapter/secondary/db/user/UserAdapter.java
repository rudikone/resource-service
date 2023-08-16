package ru.rudikov.resourceservice.adapter.secondary.db.user;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.User;
import ru.rudikov.resourceservice.application.domain.model.auth.jwt.Role;
import ru.rudikov.resourceservice.application.domain.model.dto.UserDto;
import ru.rudikov.resourceservice.application.domain.model.dto.UserManagementDto;
import ru.rudikov.resourceservice.application.port.secondary.UserPort;
import ru.rudikov.resourceservice.application.service.transform.UserMapper;

import java.util.Comparator;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserAdapter implements UserPort {

    private final UserRepository repository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserManagementDto> getByLogin(@NonNull String login) {
        return repository.getUserByLogin(login).map(userMapper::toUserManagementDto);
    }

    @Override
    public Mono<String> create(UserManagementDto user) {
        val entity = userMapper.toUser(user);
        return repository.save(entity).map(e -> entity.getId());
    }

    @Override
    public Flux<UserDto> getAllUsers() {
        return repository.findAll().map(userMapper::toUserDto);
    }

    @Override
    public Mono<UserDto> getUserById(String userId) {
        return repository.getUserById(userId).map(userMapper::toUserDto);
    }

    @Override
    @Transactional
    public Mono<UserManagementDto> updateUserById(String userId, UserManagementDto user) {
        return repository.getUserById(userId)
                .flatMap(existingUser -> {
                            existingUser.setName(user.getName());
                            existingUser.setAge(user.getAge());
                            existingUser.setSalary(user.getSalary());
                            existingUser.setDepartment(user.getDepartment());
                            existingUser.setLogin(user.getLogin());
                            existingUser.setPassword(user.getPassword());
                            existingUser.setRoles(user.getRoles().stream().map(Role::getAuthority).collect(Collectors.toSet()));
                            return repository.save(existingUser);
                        }
                ).map(userMapper::toUserManagementDto);
    }

    @Override
    public Mono<UserDto> deleteUserById(String userId) {
        return repository.findById(userId).flatMap(existingUser -> repository.delete(existingUser)
                .then(Mono.just(userMapper.toUserDto(existingUser))));
    }

    @Override
    public Flux<UserDto> searchUsers(String name) {
        return repository.findAll()
                .filter(user -> user.getName().contains(name))
                .sort(Comparator.comparingInt(User::getAge))
                .map(userMapper::toUserDto);
    }
}
