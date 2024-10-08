package com.fallt.news_service.service;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.entity.Role;
import com.fallt.news_service.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NewsAccessCheckerService implements AccessCheckerService {

    private final NewsRepository newsRepository;

    @Override
    public boolean accessCheck(Accessible accessible, Long idFromPathVariable) {
        Long userId = getAuthenticatedUserId();
        if (accessible.onlyOwnerAccess()) {
            Long id = newsRepository.findUserIdByNewsId(idFromPathVariable);
            return id.equals(userId);
        }
        if (getRoles().stream().anyMatch(r -> r.equals(Role.ROLE_MODERATOR) || r.equals(Role.ROLE_ADMIN))) {
            return true;
        }
        return newsRepository.findUserIdByNewsId(idFromPathVariable).equals(userId);
    }

    @Override
    public CheckType getType() {
        return CheckType.NEWS;
    }
}
