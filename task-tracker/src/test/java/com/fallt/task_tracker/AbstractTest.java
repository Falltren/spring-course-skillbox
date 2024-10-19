package com.fallt.task_tracker;

import com.fallt.task_tracker.entity.RoleType;
import com.fallt.task_tracker.entity.User;
import com.fallt.task_tracker.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureWebTestClient
public class AbstractTest {

    protected final static String FIRST_USER_ID = "1";
    protected final static String SECOND_USER_ID = "2";

    @Container
    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0.8")
            .withReuse(true);

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Autowired
    protected WebTestClient webTestClient;

    @Autowired
    protected UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.saveAll(List.of(
                new User(FIRST_USER_ID, "Name 1", "user@user.user", "user", Set.of(RoleType.ROLE_USER)),
                new User(SECOND_USER_ID, "Name 2", "manager@manager.mg", "manager", Set.of(RoleType.ROLE_MANAGER)))
        ).collectList().block();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll().block();
    }

}
