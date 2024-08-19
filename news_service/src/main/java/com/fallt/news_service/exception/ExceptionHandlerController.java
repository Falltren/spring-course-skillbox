package com.fallt.news_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Objects;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRs handleBadRequestEx(Exception e) {
        return ExceptionRs.builder()
                .error("BadRequest")
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build();
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionRs handleAccessDeniedEx(Exception e) {
        return ExceptionRs.builder()
                .error("AccessDenied")
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build();
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRs handleEntityNotFoundEx(Exception e) {
        return ExceptionRs.builder()
                .error("Not found")
                .timestamp(System.currentTimeMillis())
                .errorDescription(e.getMessage())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionRs handleValidationException(MethodArgumentNotValidException ex) {
        String cause = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
        return ExceptionRs.builder()
                .error("BadRequest")
                .timestamp(System.currentTimeMillis())
                .errorDescription(cause)
                .build();
    }
}
