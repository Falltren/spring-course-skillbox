package com.fallt.news_service.controller;

import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.mapper.CommentMapper;
import com.fallt.news_service.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    public CommentRs getComment(@PathVariable Long id) {
        return CommentMapper.INSTANCE.toDto(commentService.getComment(id));
    }

    @GetMapping
    public List<CommentRs> getCommentsByNews(@RequestParam("newsId") Long id) {
        return CommentMapper.INSTANCE.toListDto(commentService.getAllCommentsByNews(id));
    }

    @PostMapping
    public CommentRs createComment(@Valid @RequestBody CommentRq request) {
        return CommentMapper.INSTANCE.toDto(commentService.createComment(request));
    }

    @PutMapping
    public CommentRs updateComment(@Valid @RequestBody UpdateCommentRq request) {
        return CommentMapper.INSTANCE.toDto(commentService.updateComment(request));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable Long id) {
        commentService.delete(id);
    }
}
