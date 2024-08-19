package com.fallt.news_service.repository;

import com.fallt.news_service.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getAllByNewsId(Long id);
}
