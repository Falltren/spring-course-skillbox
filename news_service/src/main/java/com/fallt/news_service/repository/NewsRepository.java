package com.fallt.news_service.repository;

import com.fallt.news_service.model.News;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsRepository extends JpaRepository<News, Long> {
}
