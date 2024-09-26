package com.fallt.task_tracker.exception;

import com.fallt.task_tracker.dto.ExceptionDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(EntityNotFoundException.class)
    public Mono<ResponseEntity<ExceptionDto>> entityNotFoundExceptionHandler(Exception e) {
        return Mono.just(ResponseEntity.badRequest().body(
                ExceptionDto.builder()
                        .error("BadRequest")
                        .message(e.getMessage())
                        .timestamp(System.currentTimeMillis())
                        .build()
        ));
    }


}
