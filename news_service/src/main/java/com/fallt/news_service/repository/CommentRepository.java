package com.fallt.news_service.repository;

import com.fallt.news_service.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getAllByNewsId(Long id);

    @Query("SELECT c.news.user.id FROM Comment c WHERE c.id = :commentId")
    Long findUserIdByCommentId(@Param("commentId") Long commentId);
}
