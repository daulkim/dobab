package com.du.dobab.common.exception;

import lombok.Getter;

@Getter
public enum CustomValidation {

    INVALID_MEAL_TIME("startTime", "식사 시작시간이 10분 보다 적게 남았습니다."),
    INVALID_MEAL_STATUS("mealStatus", "해당 식사는 참여할 수 없습니다."),
    ;

    private String fieldName;
    private String errorMessage;

    CustomValidation(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
}
