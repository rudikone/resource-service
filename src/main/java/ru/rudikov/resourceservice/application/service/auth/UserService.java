package ru.rudikov.resourceservice.application.service.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rudikov.resourceservice.application.domain.model.dto.Role;
import ru.rudikov.resourceservice.application.domain.model.dto.User;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final List<User> users; //использовать хранилище

    public UserService() {
        this.users = List.of(
                new User("rudikov", "1234", Collections.singleton(Role.USER)),
                new User("ivanov", "12345", Collections.singleton(Role.ADMIN))
        );
    }

    public Optional<User> getByLogin(@NonNull String login) {
        return users.stream()
                .filter(user -> login.equals(user.getLogin()))
                .findFirst();
    }

}
