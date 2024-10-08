package com.fallt.news_service.controller;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.dto.request.CommentRq;
import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.response.CommentRs;
import com.fallt.news_service.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public CommentRs getComment(@PathVariable Long id) {
        return commentService.getCommentById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public List<CommentRs> getCommentsByNews(@RequestParam("newsId") Long id) {
        return commentService.getAllCommentsByNews(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public CommentRs createComment(@Valid @RequestBody CommentRq request) {
        return commentService.createComment(request);
    }

    @Accessible(checkType = CheckType.COMMENT, onlyOwnerAccess = true)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public CommentRs updateComment(@Valid @PathVariable Long id, @RequestBody UpdateCommentRq request) {
        return commentService.updateComment(id, request);
    }

    @Accessible(checkType = CheckType.COMMENT)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR', 'ROLE_USER')")
    public void deleteComment(@PathVariable Long id) {
        commentService.delete(id);
    }
}
