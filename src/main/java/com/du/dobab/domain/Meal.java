package com.du.dobab.domain;

import com.du.dobab.dto.MealStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(name = "MEAL")
public class Meal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEAL_ID")
    private Long id;

    @Column(name="USER_ID")
    private String userId;

    @Column(name="TITLE")
    private String title;

    @Column(name="CONTENTS")
    private String contents;

    @Column(name="START_TIME")
    private LocalTime startTime;

    @Column(name = "MEAL_TIME")
    private int mealTime;

    @Column(name="STATUS")
    private Enum status;

    @Embedded
    private Location location;

    @Builder
    public Meal(String userId, String title, String contents,
                LocalTime startTime, int mealTime,
                MealStatus status, Location location) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.startTime = startTime;
        this.mealTime = mealTime;
        this.status = status;
        this.location = location;
    }

    public boolean isOpened() {
        return this.status == MealStatus.OPEN;
    }
}
