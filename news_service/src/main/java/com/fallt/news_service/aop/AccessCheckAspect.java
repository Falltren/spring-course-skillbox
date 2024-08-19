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

//    @Before("@annotation(com.fallt.news_service.aop.Accessible)")
//    public void check(JoinPoint joinPoint) {
//        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
//        assert requestAttributes != null;
//        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
//        String userIdFromHeader = request.getHeader("user");
//        UpdateNewsRq dto = (UpdateNewsRq) joinPoint.getArgs()[0];
//        Long userIdFromDto = dto.getUserId();
//        if (!userIdFromHeader.equals(userIdFromDto.toString())) {
//            throw new AccessDeniedException("Вы можете редактировать/удалять только новости, автором которых являетесь вы");
//        }
//    }
//
//    private String checkArgument(JoinPoint joinPoint) {
//        Object type = joinPoint.getArgs()[0];
//        if (type instanceof Integer){
//            return type.toString();
//        } else if (type instanceof UpdateNewsRq updateNewsRq) {
//            return updateNewsRq.getId().toString();
//        } else if (type instanceof UpdateCommentRq updateCommentRq) {
//
//        }
//    }
}
