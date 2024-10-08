package com.fallt.news_service.controller;

import com.fallt.news_service.AbstractTest;
import com.fallt.news_service.dto.request.CategoryDto;
import com.fallt.news_service.repository.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Sql(scripts = "/sql/categorycontroller_before_test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/categorycontroller_after_test.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
class CategoryControllerTest extends AbstractTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Создание категории пользователем с ролью ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminCreateCategory_thenReturnOk() throws Exception {
        CategoryDto request = new CategoryDto("New category");
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New category"));
        assertEquals(3L, categoryRepository.count());
    }

    @Test
    @DisplayName("Попытка создания категории пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserCreateCategory_thenReturnForbidden() throws Exception {
        CategoryDto request = CategoryDto.builder()
                .title("New category")
                .build();
        String content = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/categories/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Получение всех категорий")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetAllCategories_thenReturnOk() throws Exception {
        List<CategoryDto> expected = List.of(
                new CategoryDto("Animal"),
                new CategoryDto("Universe")
        );
        mockMvc.perform(get("/api/v1/categories?offset=0&limit=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Получение категории по ID")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenGetCategoryById_thenReturnOk() throws Exception {
        CategoryDto expected = new CategoryDto("Universe");
        mockMvc.perform(get("/api/v1/categories/101"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(expected)));
    }

    @Test
    @DisplayName("Обновление категории пользователем с ролью ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminUpdateCategory_thenReturnOk() throws Exception {
        Long id = 101L;
        CategoryDto updateCategory = new CategoryDto("New category");
        String content = objectMapper.writeValueAsString(updateCategory);
        mockMvc.perform(put("/api/v1/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isOk());
        String actualTitle = categoryRepository.findById(id).orElseThrow().getTitle();
        assertEquals("New category", actualTitle);
    }

    @Test
    @DisplayName("Попытка обновления категории пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserUpdateCategory_thenReturnForbidden() throws Exception {
        Long id = 101L;
        CategoryDto updateCategory = new CategoryDto("New category");
        String content = objectMapper.writeValueAsString(updateCategory);
        mockMvc.perform(put("/api/v1/categories/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andDo(print())
                .andExpect(status().isForbidden());
        String actualTitle = categoryRepository.findById(id).orElseThrow().getTitle();
        assertEquals("Universe", actualTitle);
    }

    @Test
    @DisplayName("Удаление категории пользователем с ролью ADMIN")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenAdminDeleteCategory_thenReturnOk() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/100"))
                .andDo(print())
                .andExpect(status().isOk());
        assertEquals(1L, categoryRepository.count());
    }

    @Test
    @DisplayName("Попытка удаления категории пользователем с ролью USER")
    @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl", value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    void whenUserDeleteCategory_thenReturnForbidden() throws Exception {
        mockMvc.perform(delete("/api/v1/categories/100"))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

}
