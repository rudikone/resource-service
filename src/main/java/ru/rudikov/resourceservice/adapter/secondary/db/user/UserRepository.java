package ru.rudikov.resourceservice.adapter.secondary.db.user;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;
import ru.rudikov.resourceservice.adapter.secondary.db.entity.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Mono<User> getUserByLogin(String login);

    Mono<User> getUserById(String id);

}
