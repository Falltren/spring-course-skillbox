package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.exception.BadRequestException;
import com.fallt.news_service.mapper.NewsMapper;
import com.fallt.news_service.model.Category;
import com.fallt.news_service.model.News;
import com.fallt.news_service.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NewsService {

    private final NewsRepository newsRepository;

    private final CategoryService categoryService;

    public OneNewsRs create(NewsRq newsRq) {
        News news = newsRepository.save(NewsMapper.INSTANCE.toEntity(newsRq));
        Category category = categoryService.save(newsRq.getCategory());
        news.setCategory(category);
        return NewsMapper.INSTANCE.toDto(news);
    }

    public List<SomeNewsRs> getAllNews(Integer offset, Integer limit) {
        List<News> news = newsRepository.findAll(PageRequest.of(offset, limit)).getContent();
        return NewsMapper.INSTANCE.toListDto(news);
    }

    public OneNewsRs getNews(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new BadRequestException(MessageFormat.format("Новость с ID: {0} не существует", id));
        }
        return NewsMapper.INSTANCE.toDto(optionalNews.get());
    }

    public OneNewsRs update(UpdateNewsRq request) {
        Long id = request.getNewsId();
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new BadRequestException(MessageFormat.format("Новость с ID: {0} не существует", id));
        }
        News news = optionalNews.get();
        NewsMapper.INSTANCE.updateNewsFromDto(request, news);
        Category category = categoryService.update(news.getCategory(), request.getCategory());
        news.setCategory(category);
        return NewsMapper.INSTANCE.toDto(news);
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
