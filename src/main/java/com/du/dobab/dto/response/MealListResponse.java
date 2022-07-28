package com.du.dobab.dto.response;

import com.du.dobab.domain.Meal;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MealListResponse {

    private String userId;
    private String title;
    private LocalDateTime startDatetime;

    public MealListResponse(Meal entity) {
        this.userId = entity.getUserId();
        this.title = entity.getTitle();
        this.startDatetime = entity.getStartDatetime();
    }
}
