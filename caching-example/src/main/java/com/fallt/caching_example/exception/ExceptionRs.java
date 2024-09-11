package com.fallt.caching_example.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionRs {

    private String message;

    private Long timestamp;
}
