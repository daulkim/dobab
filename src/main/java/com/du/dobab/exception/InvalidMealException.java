package com.du.dobab.exception;

import com.du.dobab.common.exception.CustomValidation;

public class InvalidMealException extends CustomException {

    private static final String MESSAGE = "잘못된 요청입니다.";

    public InvalidMealException(CustomValidation customValidation) {
        super(MESSAGE);
        addValidation(customValidation.getFieldName(), customValidation.getErrorMessage());
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
