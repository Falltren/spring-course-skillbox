package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.CategoryDto;
import com.fallt.news_service.dto.request.NewsFilter;
import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.entity.Category;
import com.fallt.news_service.entity.News;
import com.fallt.news_service.entity.User;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.NewsMapper;
import com.fallt.news_service.repository.NewsRepository;
import com.fallt.news_service.repository.NewsSpecification;
import com.fallt.news_service.security.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public OneNewsRs create(NewsRq newsRq) {
        News news = NewsMapper.INSTANCE.toEntity(newsRq);
        Category category = categoryService.createOrGetExist(new CategoryDto(newsRq.getCategory()));
        news.setCategory(category);
        AppUserDetails currentUser = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.findUser(currentUser.getId());
        news.setUser(user);
        return NewsMapper.INSTANCE.toDto(newsRepository.save(news));
    }

    public List<SomeNewsRs> getAllNews(Integer offset, Integer limit) {
        return NewsMapper.INSTANCE.toListDto(newsRepository.findAll(PageRequest.of(offset, limit)).getContent());
    }

    public OneNewsRs getNewsById(Long id) {
        return NewsMapper.INSTANCE.toDto(getNews(id));
    }

    public News getNews(Long id) {
        Optional<News> optionalNews = newsRepository.findById(id);
        if (optionalNews.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Новость с ID: {0} не существует", id));
        }
        return optionalNews.get();
    }

    public List<SomeNewsRs> filterBy(NewsFilter newsFilter) {
        return NewsMapper.INSTANCE.toListDto(newsRepository.findAll(NewsSpecification.withFilter(newsFilter),
                PageRequest.of(newsFilter.getOffset(), newsFilter.getLimit())).getContent());
    }

    @Transactional
    public OneNewsRs update(Long id, UpdateNewsRq request) {
        News news = getNews(id);
        NewsMapper.INSTANCE.updateNewsFromDto(request, news);
        Category existedCategory = news.getCategory();
        Category updatedCategory = categoryService.changeCategory(existedCategory, request.getCategory());
        news.setCategory(updatedCategory);
        return NewsMapper.INSTANCE.toDto(newsRepository.save(news));
    }

    public void delete(Long id) {
        newsRepository.deleteById(id);
    }
}
