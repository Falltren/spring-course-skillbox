package com.fallt.news_service.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionRs {

    private String error;

    private Long timestamp;

    @JsonProperty("error_description")
    private String errorDescription;
}
