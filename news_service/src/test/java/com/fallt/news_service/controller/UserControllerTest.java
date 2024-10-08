package com.fallt.news_service.controller;

import com.fallt.news_service.AbstractTest;
import com.fallt.news_service.dto.request.RegisterRq;
import com.fallt.news_service.dto.request.UserUpdateRq;
import com.fallt.news_service.entity.Role;
import com.fallt.news_service.exception.AccessDeniedException;
import com.fallt.news_service.exception.BadRequestException;
import com.fallt.news_service.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(scripts = "/sql/usercontroller_before_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usercontroller_after_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class UserControllerTest extends AbstractTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Корректное создание пользователя")
    void whenUserCorrectRegister_thenReturnCreated() throws Exception {
        RegisterRq request = RegisterRq.builder()
                .name("newUser")
                .email("test@test.test")
                .password("Testuser1")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("newUser"));
        assertEquals(3, userRepository.count());
    }

    @Test
    @DisplayName("Создание пользователя с некорректным паролем")
    void whenUserRegisterWithIncorrectPassword_thenReturnBadRequest() throws Exception {
        RegisterRq request = RegisterRq.builder()
                .name("testUser")
                .email("test@test.test")
                .password("test1")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Попытка создания пользователя с именем, которое уже присутствует в базе данных")
    void whenUserRegisterWithNameWhichExistInDb_thenReturnBadRequest() throws Exception {
        RegisterRq request = RegisterRq.builder()
                .name("testUser")
                .email("test@test.test")
                .password("Testuser1")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(BadRequestException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Попытка создания пользователя с эл. почтой, которая уже присутствует в базе данных")
    void whenUserRegisterWithEmailWhichExistInDb_thenReturnBadRequest() throws Exception {
        RegisterRq request = RegisterRq.builder()
                .name("user")
                .email("testuser@user.com")
                .password("Testuser1")
                .roles(Set.of(Role.ROLE_USER))
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/users/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertInstanceOf(BadRequestException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Получение данных о пользователе по ID, принадлежащем пользователю")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserRequestGetByIdAndThisIdBelongsUser_thenReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/users/100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testUser"));
    }

    @Test
    @DisplayName("Получение данных о пользователе по ID, не принадлежащем пользователю")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserRequestGetByIdAndThisIdNotBelongsUser_thenReturnAccessDenied() throws Exception {
        mockMvc.perform(get("/api/v1/users/101"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(AccessDeniedException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Получение данных о пользователе аккаунтом, имеющим роль ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminRequestGetById_thenReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/users/100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("testUser"));
    }

    @Test
    @DisplayName("Получение данных о всех пользователях, пользователем с ролью ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminRequestAllUsers_thenReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Получение данных о всех пользователях, пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserRequestAllUsers_thenReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/users"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Обновление пользователем данных о себе")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserUpdate_thenReturnOk() throws Exception {
        UserUpdateRq userUpdateRq = UserUpdateRq.builder()
                .name("updateName")
                .build();
        String content = objectMapper.writeValueAsString(userUpdateRq);
        mockMvc.perform(put("/api/v1/users/update/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updateName"));

    }

    @Test
    @DisplayName("Попытка обновления пользователем с ролью USER данных о другом пользователе")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserUpdateAnotherUser_thenReturnForbidden() throws Exception {
        UserUpdateRq userUpdateRq = UserUpdateRq.builder()
                .name("updateName")
                .build();
        String content = objectMapper.writeValueAsString(userUpdateRq);
        mockMvc.perform(put("/api/v1/users/update/101")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(AccessDeniedException.class, result.getResolvedException()));
    }


    @Test
    @DisplayName("Попытка удаления другого пользователя, пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserDeleteAnotherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/users/101"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(AccessDeniedException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Удаление пользователем своего аккаунта")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserDelete_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/users/100"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(1L, userRepository.count());
    }

    @Test
    @DisplayName("Попытка удаления аккаунта неавторизованным пользователем")
    @WithAnonymousUser
    void whenUnauthorizedUserDelete_thenReturnUnauthorized() throws Exception {
        mockMvc.perform(delete("/api/v1/users/1"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
