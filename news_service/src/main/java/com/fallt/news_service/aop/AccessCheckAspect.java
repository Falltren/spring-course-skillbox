package com.fallt.news_service.aop;

import com.fallt.news_service.exception.AccessDeniedException;
import com.fallt.news_service.service.AccessCheckerFactory;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessCheckAspect {

    private final AccessCheckerFactory accessCheckerFactory;

    @Before("@annotation(accessible)")
    @SuppressWarnings("unchecked")
    public void check(Accessible accessible) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long idFromPathVariable = Long.valueOf(pathVariables.get("id"));
        if (!accessCheckerFactory.isAccessDenied(accessible, idFromPathVariable)) {
            throw new AccessDeniedException("Вы не можете выполнить это действие");
        }
    }
}
