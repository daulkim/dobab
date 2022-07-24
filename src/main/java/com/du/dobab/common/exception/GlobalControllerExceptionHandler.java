package com.du.dobab.common.exception;

import com.du.dobab.exception.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalControllerExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                                                    .code("400")
                                                    .message("잘못된 요청입니다.")
                                                    .build();
        for(FieldError fieldError:e.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return errorResponse;
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> customExceptionHandler(CustomException e) {
        ErrorResponse body = ErrorResponse.builder()
                                            .code(String.valueOf(e.getStatusCode()))
                                            .message(e.getMessage())
                                            .validation(e.getValidation())
                                            .build();

        ResponseEntity<ErrorResponse> errorResponse = ResponseEntity.status(e.getStatusCode())
                                                                    .body(body);
        return errorResponse;
    }
}
