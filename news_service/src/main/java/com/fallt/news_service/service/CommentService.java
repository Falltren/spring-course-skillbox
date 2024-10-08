package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.entity.Comment;
import com.fallt.news_service.entity.News;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.CommentMapper;
import com.fallt.news_service.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    private final NewsService newsService;

    private final UserService userService;

    public CommentRs createComment(CommentRq commentRq) {
        Comment comment = CommentMapper.INSTANCE.toEntity(commentRq);
        News news = newsService.getNews(commentRq.getNewsId());
        comment.setNews(news);
        comment.setUser(userService.findUser(userService.getIdCurrentUser()));
        commentRepository.save(comment);
        return CommentMapper.INSTANCE.toDto(comment);
    }

    private Comment getComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Комментарий с ID: {0} не существует", id));
        }
        return optionalComment.get();
    }

    public CommentRs getCommentById(Long id) {
        return CommentMapper.INSTANCE.toDto(getComment(id));
    }

    public List<CommentRs> getAllCommentsByNews(Long id) {
        return CommentMapper.INSTANCE.toListDto(commentRepository.getAllByNewsId(id));
    }

    public CommentRs updateComment(Long id, UpdateCommentRq request) {
        Comment comment = getComment(id);
        CommentMapper.INSTANCE.updateCommentFromDto(request, comment);
        return CommentMapper.INSTANCE.toDto(commentRepository.save(comment));
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
