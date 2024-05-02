package com.fallt.news_service.controller;

import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/create")
    public OneNewsRs create(@RequestBody NewsRq newsRq) {
        return newsService.create(newsRq);
    }

    @GetMapping
    public List<SomeNewsRs> getAll(@RequestParam Integer offset, @RequestParam Integer limit) {
        return newsService.getAllNews(offset, limit);
    }

    @GetMapping("/{id}")
    public OneNewsRs getNews(@PathVariable Long id) {
        return newsService.getNews(id);
    }

    @PutMapping
    public OneNewsRs update(@RequestBody UpdateNewsRq request) {
        return newsService.update(request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }
}
