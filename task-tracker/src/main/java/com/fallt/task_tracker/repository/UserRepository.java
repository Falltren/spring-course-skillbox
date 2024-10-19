package com.fallt.task_tracker.repository;

import com.fallt.task_tracker.entity.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

@Repository
public interface UserRepository extends ReactiveMongoRepository<User, String> {

    Flux<User> getUsersByIdIn(Set<String> ids);

    Mono<User> findByUsername(String username);
}
