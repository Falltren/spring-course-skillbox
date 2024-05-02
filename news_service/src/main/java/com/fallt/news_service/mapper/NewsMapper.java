package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.NewsRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.dto.response.OneNewsRs;
import com.fallt.news_service.dto.response.SomeNewsRs;
import com.fallt.news_service.model.Comment;
import com.fallt.news_service.model.News;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface NewsMapper {

    NewsMapper INSTANCE = getMapper(NewsMapper.class);

    @Mapping(target = "title", source = "title")
    @Mapping(target = "text", source = "text")
    @BeanMapping(ignoreByDefault = true)
    News toEntity(NewsRq newsRq);

    @Mapping(target = "category", expression = "java(news.getCategory().getTitle())")
    OneNewsRs toDto(News news);

    List<SomeNewsRs> toListDto(List<News> newsList);

    @Named("calculateCount")
    default int calculateCount(List<Comment> comments) {
        return comments.size();
    }

    @Mapping(target = "category", source = "category.title")
    @Mapping(target = "count", source = "comments", qualifiedByName = "calculateCount")
    SomeNewsRs getCategory(News news);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateNewsFromDto(UpdateNewsRq request, @MappingTarget News news);

}
