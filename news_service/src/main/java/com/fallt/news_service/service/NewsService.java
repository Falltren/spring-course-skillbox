package com.fallt.news_service.service;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.dto.request.CategoryDto;
import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.NewsMapper;
import com.fallt.news_service.model.Category;
import com.fallt.news_service.model.News;
import com.fallt.news_service.model.User;
import com.fallt.news_service.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final UserService userService;

    private final CategoryService categoryService;

    @Transactional
    public News create(NewsRq newsRq) {
        News news = NewsMapper.INSTANCE.toEntity(newsRq);
        Category category = categoryService.create(new CategoryDto(newsRq.getCategory()));
        news.setCategory(category);
        User user = userService.getUserById(newsRq.getUserId());
        news.setUser(user);
        return newsRepository.save(news);
    }

    public List<News> getAllNews(Integer offset, Integer limit) {
        return newsRepository.findAll(PageRequest.of(offset, limit)).getContent();
    }

    public News getNews(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Новость с ID: {0} не существует", id));
        }
        return optionalNews.get();
    }

    @Accessible
    @Transactional
    public News update(UpdateNewsRq request) {
        News news = getNews(request.getId());
        NewsMapper.INSTANCE.updateNewsFromDto(request, news);
        Category existedCategory = news.getCategory();
        Category updatedCategory = categoryService.changeCategory(existedCategory, request.getCategory());
        news.setCategory(updatedCategory);
        return newsRepository.save(news);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
