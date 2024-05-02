package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.model.Comment;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "text", source = "text")
    @BeanMapping(ignoreByDefault = true)
    Comment toEntity(CommentRq commentRq);

    CommentRs toDto(Comment comment);
}
