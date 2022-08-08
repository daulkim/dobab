package com.du.dobab.common.exception;

import lombok.Getter;

@Getter
public enum CustomValidation {

    INVALID_MEAL_TIME("startTime", "식사 시작시간이 10분 보다 적게 남았습니다."),
    INVALID_JOIN_STATUS("joinStatus", "해당 식사에 참여할 수 없습니다."),
    INVALID_MEAL_SAVE("mealTime", "해당 식사를 등록할 수 없습니다."),
    INVALID_MEAL_DELETE_STATUS("mealDeleteStatus", "해당 식사는 삭제할 수 없는 상태 입니다."),
    ;

    private String fieldName;
    private String errorMessage;

    CustomValidation(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }
}
