package com.fallt.news_service.service;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AccessCheckerFactory {

    private final Map<CheckType, AccessCheckerService> accessCheckerServiceMap;

    private AccessCheckerService getAccessChecker(CheckType checkType) {
        AccessCheckerService accessCheckerService = accessCheckerServiceMap.get(checkType);
        if (accessCheckerService == null) {
            throw new RuntimeException("Передан некорректный проверяемый тип: " + checkType.name());
        }
        return accessCheckerService;
    }

    public boolean isAccessDenied(Accessible accessible, Long idFromPathVariable) {
        AccessCheckerService accessCheckerService = getAccessChecker(accessible.checkType());
        return accessCheckerService.accessCheck(accessible, idFromPathVariable);
    }
}
