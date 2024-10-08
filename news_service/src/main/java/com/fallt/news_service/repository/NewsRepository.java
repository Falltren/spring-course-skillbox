package com.fallt.news_service.repository;

import com.fallt.news_service.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NewsRepository extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {

    @Query("SELECT n.user.id FROM News n WHERE n.id = :newsId")
    Long findUserIdByNewsId(@Param("newsId") Long newsId);
}
