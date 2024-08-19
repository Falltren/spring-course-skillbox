package com.fallt.news_service.repository;

import com.fallt.news_service.dto.request.NewsFilter;
import com.fallt.news_service.model.News;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {

    static Specification<News> withFilter(NewsFilter filter) {
        return Specification.where(byCategory(filter.getCategory()))
                .and(byAuthor(filter.getAuthor()));
    }

    static Specification<News> byCategory(String category) {
        return ((root, query, criteriaBuilder) -> {
            if (category == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("category").get("title"), category);
        });
    }

    static Specification<News> byAuthor(String author) {
        return ((root, query, criteriaBuilder) -> {
            if (author == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("name"), author);
        });
    }
}
