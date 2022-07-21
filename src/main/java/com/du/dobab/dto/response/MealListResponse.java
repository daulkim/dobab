package com.du.dobab.dto.response;

import com.du.dobab.domain.Meal;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class MealListResponse {

    private String userId;
    private String title;
    private LocalTime startTime;

    public MealListResponse(Meal entity) {
        this.userId = entity.getUserId();
        this.title = entity.getTitle();
        this.startTime = entity.getStartTime();
    }
}
