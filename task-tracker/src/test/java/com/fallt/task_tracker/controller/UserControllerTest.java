package com.fallt.task_tracker.controller;

import com.fallt.task_tracker.AbstractTest;
import com.fallt.task_tracker.dto.UserRq;
import com.fallt.task_tracker.dto.UserRs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserControllerTest extends AbstractTest {

    @Test
    @DisplayName("Получение пользователя по ID")
    @WithMockUser("Name 1")
    void whenGetUserByID_thenReturnOk() {
        UserRs expected = UserRs.builder()
                .username("Name 1")
                .email("user@user.user")
                .build();

        webTestClient.get().uri("/api/v1/users/{id}", FIRST_USER_ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserRs.class)
                .isEqualTo(expected);
    }

    @Test
    @DisplayName("Попытка получения несуществующего пользователя по ID")
    @WithMockUser("Name 1")
    void whenGetUserByIncorrectId_thenReturnBadRequest() {
        webTestClient.get().uri("/api/v1/users/{id}", 100)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Получение всех пользователей")
    @WithMockUser("Name 2")
    void whenGetAllUsers_thenReturnListUsersFromDatabase() {
        var expected = List.of(
                new UserRs("Name 1", "user@user.user"),
                new UserRs("Name 2", "manager@manager.mg")
        );

        webTestClient.get().uri("/api/v1/users")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(UserRs.class)
                .hasSize(2)
                .contains(expected.toArray(UserRs[]::new));
    }

    @Test
    @DisplayName("Создание нового пользователя")
    @WithAnonymousUser
    void whenCreateUser_thenReturnCreated() {
        UserRq request = UserRq.builder()
                .username("new user")
                .email("new@user.user")
                .password("12345")
                .build();
        UserRs expected = UserRs.builder()
                .username("new user")
                .email("new@user.user")
                .build();
        StepVerifier.create(userRepository.count())
                .expectNext(2L)
                .expectComplete()
                .verify();

        webTestClient.post().uri("/api/v1/users?roleType=ROLE_USER")
                .body(Mono.just(request), UserRq.class)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserRs.class)
                .isEqualTo(expected);
        StepVerifier.create(userRepository.count())
                .expectNext(3L)
                .expectComplete()
                .verify();
    }

    @Test
    @DisplayName("Обновление данных о пользователе")
    @WithMockUser("Name 1")
    void whenUpdateUser_thenReturnUpdatedUser() {
        UserRq request = new UserRq();
        request.setUsername("Updated name");

        webTestClient.put().uri("/api/v1/users/{id}", FIRST_USER_ID)
                .body(Mono.just(request), UserRq.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(UserRs.class)
                .value(responseModel -> {
                    assertEquals("Updated name", responseModel.getUsername());
                });

        StepVerifier.create(userRepository.findByUsername("Name 1"))
                .expectNextCount(0L)
                .verifyComplete();

        StepVerifier.create(userRepository.findByUsername("Updated name"))
                .expectNextCount(1L)
                .verifyComplete();
    }

    @Test
    @DisplayName("Попытка обновления несуществующего пользователя")
    @WithMockUser("Name 1")
    void whenUpdateUserByIncorrectUserId_thenReturnBadRequest(){
        UserRq request = new UserRq();
        request.setUsername("Updated name");

        webTestClient.put().uri("/api/v1/users/{id}", 100)
                .body(Mono.just(request), UserRq.class)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    @DisplayName("Попытка обновления данных неавторизованным пользователем")
    @WithAnonymousUser
    void whenUpdateUserByAnonymous_thenReturnUnauthorized() {
        UserRq request = new UserRq();
        request.setUsername("Updated name");

        webTestClient.put().uri("/api/v1/users/{id}", FIRST_USER_ID)
                .body(Mono.just(request), UserRq.class)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    @DisplayName("Удаление пользователя")
    @WithMockUser("Name 1")
    void whenDeleteUserById_thenUserDeletedFromDatabase(){
        webTestClient.delete().uri("/api/v1/users/{id}", FIRST_USER_ID)
                .exchange()
                .expectStatus().isNoContent();

        StepVerifier.create(userRepository.count())
                .expectNext(1L)
                .expectComplete()
                .verify();
    }

}
