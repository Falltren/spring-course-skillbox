package com.fallt.news_service.service;

import com.fallt.news_service.aop.Accessible;
import com.fallt.news_service.aop.CheckType;
import com.fallt.news_service.entity.Role;
import com.fallt.news_service.security.AppUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public interface AccessCheckerService {
    default Long getAuthenticatedUserId() {
        AppUserDetails user = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

    default List<Role> getRoles() {
        AppUserDetails user = (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getAuthorities().stream()
                .map(r -> Role.valueOf(r.getAuthority()))
                .toList();
    }

    boolean accessCheck(Accessible accessible, Long idFromPathVariable);

    CheckType getType();
}
