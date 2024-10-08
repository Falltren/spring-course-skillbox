package com.fallt.news_service.controller;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.dto.request.NewsFilter;
import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.service.NewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public OneNewsRs create(@RequestBody @Valid NewsRq newsRq) {
        return newsService.create(newsRq);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public List<SomeNewsRs> getAll(@RequestParam(defaultValue = "0") Integer offset, @RequestParam(defaultValue = "1") Integer limit) {
        return newsService.getAllNews(offset, limit);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public List<SomeNewsRs> filterBy(@RequestBody NewsFilter newsFilter) {
        return newsService.filterBy(newsFilter);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public OneNewsRs getNewsById(@PathVariable Long id) {
        return newsService.getNewsById(id);
    }

    @Accessible(checkType = CheckType.NEWS, onlyOwnerAccess = true)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public OneNewsRs update(@PathVariable Long id, @RequestBody UpdateNewsRq request) {
        return newsService.update(id, request);
    }

    @Accessible(checkType = CheckType.NEWS)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public void delete(@PathVariable Long id) {
        newsService.delete(id);
    }
}
