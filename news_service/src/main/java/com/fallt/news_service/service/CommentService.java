package com.fallt.news_service.service;

import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.exception.EntityNotFoundException;
import com.fallt.news_service.mapper.CommentMapper;
import com.fallt.news_service.model.Comment;
import com.fallt.news_service.model.News;
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

    public Comment createComment(CommentRq commentRq) {
        Comment comment = CommentMapper.INSTANCE.toEntity(commentRq);
        News news = newsService.getNews(commentRq.getNewsId());
        comment.setNews(news);
        commentRepository.save(comment);
        return comment;
    }

    public Comment getComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Комментарий с ID: {0} не существует", id));
        }
        return optionalComment.get();
    }

    public List<Comment> getAllCommentsByNews(Long id) {
        return commentRepository.getAllByNewsId(id);
    }

    public Comment updateComment(UpdateCommentRq request) {
        Comment comment = getComment(request.getId());
        CommentMapper.INSTANCE.updateCommentFromDto(request, comment);
        return commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
