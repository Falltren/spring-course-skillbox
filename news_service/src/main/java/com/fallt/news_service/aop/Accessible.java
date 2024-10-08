package com.fallt.news_service.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Accessible {

    CheckType checkType() default CheckType.UNKNOWN;

    boolean onlyOwnerAccess() default false;
}
