package com.du.dobab.dto.response;

import com.du.dobab.domain.Location;
import com.du.dobab.domain.Meal;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MealResponse {

    private String userId;
    private Location location;
    private LocalDateTime startDatetime;
    private int mealTime;
    private String contents;

    public MealResponse(Meal entity) {
        this.userId = entity.getUserId();
        this.location = entity.getLocation();
        this.startDatetime = entity.getStartDatetime();
        this.mealTime = (int) ChronoUnit.HOURS.between(entity.getStartDatetime(), entity.getEndDatetime());
        this.contents = entity.getContents();
    }
}
