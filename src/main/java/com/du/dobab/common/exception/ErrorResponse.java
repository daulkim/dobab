package com.du.dobab.common.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
@Getter
public class ErrorResponse {

    private final String code;
    private final String message;
    private final Map<String,String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
