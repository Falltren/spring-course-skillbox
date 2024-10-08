package com.fallt.news_service.controller;

import com.fallt.news_service.AbstractTest;
import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.repository.CommentRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/sql/commentcontroller_before_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/commentcontroller_after_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CommentControllerTest extends AbstractTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("Добавление комментария к новости, опубликованной пользователем")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenCreateComment_thenReturnOk() throws Exception {
        CommentRq request = CommentRq.builder()
                .newsId(100L)
                .text("New comment")
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("New comment"));
        assertEquals(4L, commentRepository.count());
    }

    @Test
    @DisplayName("Добавление комментария к новости, опубликованной другим пользователем")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenCreateCommentToNewsAnotherUser_thenReturnOk() throws Exception {
        CommentRq request = CommentRq.builder()
                .newsId(101L)
                .text("News another user")
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("News another user"));

    }

    @Test
    @DisplayName("Обновление собственного комментария к новости")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUpdateOwnComment_thenReturnOk() throws Exception {
        UpdateCommentRq request = UpdateCommentRq.builder()
                .text("Update text")
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/api/v1/comments/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.text").value("Update text"));
    }

    @Test
    @DisplayName("Попытка обновления чужого комментария пользователем с ролью ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminUpdateNotOwnComment_thenReturnForbidden() throws Exception {
        UpdateCommentRq request = UpdateCommentRq.builder()
                .text("Update text by ADMIN")
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(put("/api/v1/comments/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Удаление собственного комментария")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenDeleteComment_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/100"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(2L, commentRepository.count());
    }

    @Test
    @DisplayName("Попытка удаления чужого комментария пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenDeleteCommentAnotherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/101"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Удаление чужого комментария пользователем с ролью ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminDeleteCommentAnotherUser_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/comments/100"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(2L, commentRepository.count());
    }

    @Test
    @DisplayName("Получение всех комментариев")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetAllCommentsByNews_thenReturnOk() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSX");
        List<CommentRs> expected = List.of(
                CommentRs.builder()
                        .user("testUser")
                        .text("Where is my rifle")
                        .createAt(Instant.from(formatter.parse("2024-10-03 10:00:00.000000+00")))
                        .build(),
                CommentRs.builder()
                        .user("testAdmin")
                        .text("Just comment")
                        .createAt(Instant.from(formatter.parse("2024-10-03 10:30:00.000000+00")))
                        .build()
        );
        mockMvc.perform(get("/api/v1/comments?newsId=100"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Получение одного комментария")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetCommentById_thenReturnOk() throws Exception {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSSX");
        CommentRs expected = CommentRs.builder()
                .user("testAdmin")
                .text("It is amazing!")
                .createAt(Instant.from(formatter.parse("2024-10-03 12:00:00.000000+00")))
                .build();
        mockMvc.perform(get("/api/v1/comments/101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

}
