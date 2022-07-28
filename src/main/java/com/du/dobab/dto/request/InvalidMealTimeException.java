package com.du.dobab.dto.request;

import com.du.dobab.exception.CustomException;

public class InvalidMealTimeException extends CustomException {

    private static final String MESSAGE = "식사시간이 잘못되었습니다.";

    public InvalidMealTimeException() {
        super(MESSAGE);
        addValidation("startTime", "식사 시작시간이 10분 보다 적게 남았습니다.");
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
