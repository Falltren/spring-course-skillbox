package com.fallt.news_service.mapper;

import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.entity.Comment;
import org.mapstruct.*;

import java.util.List;

import static org.mapstruct.factory.Mappers.getMapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    CommentMapper INSTANCE = getMapper(CommentMapper.class);

    @Mapping(target = "text", source = "text")
    @BeanMapping(ignoreByDefault = true)
    Comment toEntity(CommentRq commentRq);

    @Mapping(target = "user", expression = "java(comment.getUser().getName())")
    CommentRs toDto(Comment comment);

    @Mapping(target = "user", source = "comments.user.name")
    List<CommentRs> toListDto(List<Comment> comments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "news", ignore = true)
    @Mapping(target = "createAt", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    @Mapping(target = "user", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCommentFromDto(UpdateCommentRq request, @MappingTarget Comment comment);
}
