package com.fallt.news_service.controller;

import com.fallt.news_service.AbstractTest;
import com.fallt.news_service.dto.request.NewsFilter;
import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.exception.AccessDeniedException;
import com.fallt.news_service.repository.NewsRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/sql/newscontroller_before_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/newscontroller_after_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class NewsControllerTest extends AbstractTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    @DisplayName("Успешное создание новости")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenCreateNews_thenReturnOk() throws Exception {
        NewsRq newsRq = NewsRq.builder()
                .text("Lions from Uganda have completed a record swim")
                .title("King of beasts")
                .category("Animal")
                .build();
        String content = objectMapper.writeValueAsString(newsRq);
        mockMvc.perform(post("/api/v1/news/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("King of beasts"))
                .andExpect(jsonPath("$.text").value("Lions from Uganda have completed a record swim"))
                .andExpect(jsonPath("$.category").value("Animal"));
    }

    @Test
    @DisplayName("Обновление пользователем своей новости")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUpdateNews_thenReturnOk() throws Exception {
        UpdateNewsRq updateRequest = UpdateNewsRq.builder()
                .title("Update title")
                .build();
        String content = objectMapper.writeValueAsString(updateRequest);
        mockMvc.perform(put("/api/v1/news/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Update title"));
    }

    @Test
    @DisplayName("Попытка обновления новости пользователем с ролью ADMIN, автором которой он не является")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUpdateNewsAnotherUser_thenReturnForbidden() throws Exception {
        UpdateNewsRq updateRequest = UpdateNewsRq.builder()
                .title("Update title")
                .build();
        String content = objectMapper.writeValueAsString(updateRequest);
        mockMvc.perform(put("/api/v1/news/100")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(AccessDeniedException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Попытка удаления чужой новости пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenDeleteNewsAnotherUser_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/news/101"))
                .andDo(print())
                .andExpect(status().isForbidden())
                .andExpect(result -> assertInstanceOf(AccessDeniedException.class, result.getResolvedException()));
    }

    @Test
    @DisplayName("Удаление новости")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenDeleteNews_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/news/100"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(1L, newsRepository.count());
    }

    @Test
    @DisplayName("Получение новости по ID")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetById_thenReturnOk() throws Exception {
        OneNewsRs expected = OneNewsRs.builder()
                .text("Scientists have discovered a new black hole")
                .title("New black hole")
                .category("Universe")
                .comments(new ArrayList<>())
                .build();
        mockMvc.perform(get("/api/v1/news/101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Получение всех новостей")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetAllNews_thenReturnOk() throws Exception {
        List<SomeNewsRs> expected = List.of(
                SomeNewsRs.builder()
                        .title("Bird")
                        .text("The ducks have flown south")
                        .category("Animal")
                        .count(0)
                        .build(),
                SomeNewsRs.builder()
                        .title("New black hole")
                        .text("Scientists have discovered a new black hole")
                        .category("Universe")
                        .count(0)
                        .build()
        );
        mockMvc.perform(get("/api/v1/news?offset=0&limit=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Получение новостей с фильтрацией")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetByFilter_thenReturnFilteringNews() throws Exception {
        NewsFilter filter = NewsFilter.builder()
                .offset(0)
                .limit(10)
                .author("testAdmin")
                .build();
        List<SomeNewsRs> expected = List.of(
                SomeNewsRs.builder()
                        .title("New black hole")
                        .text("Scientists have discovered a new black hole")
                        .category("Universe")
                        .count(0)
                        .build());
        String content = objectMapper.writeValueAsString(filter);
        mockMvc.perform(get("/api/v1/news/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }
}
