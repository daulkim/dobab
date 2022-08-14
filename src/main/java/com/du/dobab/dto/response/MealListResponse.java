package com.du.dobab.dto.response;

import com.du.dobab.domain.Location;
import com.du.dobab.domain.Meal;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Getter
public class MealListResponse {

    private Long id;
    private String title;
    private String userId;
    private LocalDateTime startDatetime;
    private int mealTime;
    private Location location;
    private Long categoryId;
    private String categoryName;
    private String categoryImage;

    public MealListResponse(Meal entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.userId = entity.getUserId();
        this.startDatetime = entity.getStartDatetime();
        this.mealTime = (int) ChronoUnit.HOURS.between(entity.getStartDatetime(), entity.getEndDatetime());
        this.location = entity.getLocation();
        this.categoryId = entity.getCategory().getId();
        this.categoryName = entity.getCategory().getName();
        this.categoryImage = entity.getCategory().getImage();
    }
}
