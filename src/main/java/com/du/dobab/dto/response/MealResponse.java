package com.du.dobab.dto.response;

import com.du.dobab.domain.Location;
import com.du.dobab.domain.Meal;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MealResponse {

    private Long id;
    private String title;
    private String userId;
    private Location location;
    private LocalDateTime startDatetime;
    private int mealTime;
    private String contents;
    private String categoryName;

    public MealResponse(Meal entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.location = entity.getLocation();
        this.startDatetime = entity.getStartDatetime();
        this.mealTime = (int) ChronoUnit.HOURS.between(entity.getStartDatetime(), entity.getEndDatetime());
        this.title = entity.getTitle();
        this.contents = entity.getContents();
        this.categoryName = entity.getCategory().getName();
    }
}
