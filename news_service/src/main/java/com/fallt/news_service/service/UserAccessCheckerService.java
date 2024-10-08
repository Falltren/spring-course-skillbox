package com.fallt.news_service.service;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAccessCheckerService implements AccessCheckerService {

    @Override
    public boolean accessCheck(Accessible accessible, Long idFromPathVariable) {
        if (getRoles().stream().anyMatch(r -> r.equals(Role.ROLE_MODERATOR) || r.equals(Role.ROLE_ADMIN))) {
            return true;
        }
        Long userId = getAuthenticatedUserId();
        return userId.equals(idFromPathVariable);
    }

    @Override
    public CheckType getType() {
        return CheckType.USER;
    }
}
