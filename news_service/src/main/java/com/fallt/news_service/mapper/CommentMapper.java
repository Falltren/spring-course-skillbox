package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.model.Comment;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = getMapper(CommentMapper.class);

    @Mapping(target = "text", source = "text")
    @BeanMapping(ignoreByDefault = true)
    Comment toEntity(CommentRq commentRq);

    CommentRs toDto(Comment comment);

    List<CommentRs> toListDto(List<Comment> comments);

    @Mapping(target = "news", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentFromDto(UpdateCommentRq request, @MappingTarget Comment comment);
}
