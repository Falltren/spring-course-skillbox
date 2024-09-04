package com.fallt.news_service.aop;

import com.fallt.news_service.dto.request.UpdateCommentRq;
import com.fallt.news_service.dto.request.UpdateNewsRq;
import com.fallt.news_service.exception.AccessDeniedException;
import com.fallt.news_service.repository.CommentRepository;
import com.fallt.news_service.repository.NewsRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@RequiredArgsConstructor
public class AccessCheckAspect {

    private final NewsRepository newsRepository;

    private final CommentRepository commentRepository;

    @Before("@annotation(com.fallt.news_service.aop.Accessible)")
    public void check(JoinPoint joinPoint) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        assert requestAttributes != null;
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        String userIdFromHeader = request.getHeader("user");
        Long userId = getUserIdFromMethodParameters(joinPoint);
        if (!userIdFromHeader.equals(userId.toString())) {
            throw new AccessDeniedException("Вы можете редактировать/удалять только новости/комментарии, автором которых являетесь вы");
        }
    }

    private Long getUserIdFromMethodParameters(JoinPoint joinPoint) {
        CheckType checkType = getCheckType(joinPoint);
        Object type = joinPoint.getArgs()[0];
        if (type instanceof Long newsId && checkType.equals(CheckType.NEWS)) {
            return newsRepository.findUserIdByNewsId(newsId);
        } else if (type instanceof UpdateNewsRq updateNewsRq && checkType.equals(CheckType.NEWS)) {
            return newsRepository.findUserIdByNewsId(updateNewsRq.getId());
        } else if (type instanceof Long commentId && checkType.equals(CheckType.COMMENT)) {
            return commentRepository.findUserIdByCommentId(commentId);
        } else if (type instanceof UpdateCommentRq commentRq && checkType.equals(CheckType.COMMENT)) {
            return commentRepository.findUserIdByCommentId(commentRq.getId());
        } else {
            throw new IllegalArgumentException("Ошибка при определении id пользователя");
        }
    }

    private CheckType getCheckType(JoinPoint joinPoint) {
        Object target = joinPoint.getTarget();
        Class<?> targetClass = target.getClass();
        String className = targetClass.getName();
        if (className.contains("CommentService")) {
            return CheckType.COMMENT;
        }
        if (className.contains("NewsService")) {
            return CheckType.NEWS;
        }
        return CheckType.UNKNOWN;
    }
}
