package com.du.dobab.dto.request;

import com.du.dobab.common.exception.CustomValidation;
import com.du.dobab.domain.Location;
import com.du.dobab.domain.Meal;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.exception.InvalidMealException;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@Getter
public class MealSave {

    @NotBlank(message = "사용자 ID를 입력해주세요.")
    private String userId;

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    private Location location;

    @JsonFormat(pattern="HH:mm")
    @NotNull(message = "시작시간을 입력해주세요.")
    private LocalTime startTime;

    @Min(value = 1, message = "소요시간은 한시간 이상이어야 합니다.")
    private int mealTime;

    @Builder
    public MealSave(String userId, String title,
                    String contents, Location location,
                    LocalTime startTime, int mealTime) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.location = location;
        this.startTime = startTime;
        this.mealTime = mealTime;
    }

    public Meal toEntity() {
        return Meal.builder()
                    .userId(userId)
                    .title(title)
                    .contents(contents)
                    .location(location)
                    .startDatetime(startTime.atDate(LocalDate.now()))
                    .endDatetime(startTime.atDate(LocalDate.now()).plusHours(mealTime))
                    .status(MealStatus.OPEN)
                    .build();
    }

    public void validTime() {
        if(LocalTime.now().isAfter(startTime.minusMinutes(10))) {
            throw new InvalidMealException(CustomValidation.INVALID_MEAL_TIME);
        }
    }
}
