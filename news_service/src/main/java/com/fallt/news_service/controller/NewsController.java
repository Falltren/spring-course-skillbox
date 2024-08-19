package com.fallt.news_service.controller;

import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.mapper.NewsMapper;
import com.fallt.news_service.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/create")
    public OneNewsRs create(@Valid @RequestBody NewsRq newsRq) {
        return NewsMapper.INSTANCE.toDto(newsService.create(newsRq));
    }

    @GetMapping
    public List<SomeNewsRs> getAll(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1") Integer limit) {
        return NewsMapper.INSTANCE.toListDto(newsService.getAllNews(offset, limit));
    }

    @GetMapping("/{id}")
    public OneNewsRs getNews(@PathVariable Long id) {
        return NewsMapper.INSTANCE.toDto(newsService.getNews(id));
    }

    @PutMapping
    public OneNewsRs update(@Valid @RequestBody UpdateNewsRq request) {
        return NewsMapper.INSTANCE.toDto(newsService.update(request));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }
}