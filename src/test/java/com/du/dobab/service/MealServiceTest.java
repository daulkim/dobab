package com.du.dobab.service;

import com.du.dobab.domain.Meal;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.dto.response.MealResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class MealServiceTest {

    @Autowired
    private MealService mealService;

    @Autowired
    private MealRepository mealRepository;

    @AfterEach
    public void clean() {
        mealRepository.deleteAll();
    }

    @Test
    @DisplayName("등록")
    public void save_succ() {
        String userId = "user";
        String content = "test 글입니다.";
        MealSave mealSave = MealSave.builder()
                                    .userId(userId)
                                    .title("user test")
                                    .startTime(LocalTime.now())
                                    .mealTime(3)
                                    .contents(content)
                                    .build();

        mealService.save(mealSave);

        Meal savedMeal = mealRepository.findAll().get(0);
        assertEquals(1L, mealRepository.count());
        assertEquals(savedMeal.getUserId(), userId);
        assertEquals(savedMeal.getMealTime(), 3);
        assertEquals(savedMeal.getContents(), content);
    }

    @Test
    @DisplayName("1건 조회")
    public void get_succ() {
        String userId = "user";
        String content = "test 글입니다.";
        Meal meal = Meal.builder()
                        .userId(userId)
                        .title("user test")
                        .contents(content)
                        .startTime(LocalTime.now())
                        .mealTime(3)
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        MealResponse savedMeal = mealService.findById(meal.getId());

        assertEquals(1L, mealRepository.count());
        assertEquals(savedMeal.getUserId(), userId);
        assertEquals(savedMeal.getMealTime(), 3);
        assertEquals(savedMeal.getContents(), content);
    }

    @Test
    @DisplayName("글 전체 조회")
    public void list_succ() {
        Meal meal1 = Meal.builder()
                        .userId("user1")
                        .title("user1 test")
                        .contents("user1 test contents")
                        .startTime(LocalTime.now())
                        .mealTime(3)
                        .status(MealStatus.OPEN)
                        .build();
        Meal meal2 = Meal.builder()
                        .userId("user2")
                        .title("user2 test")
                        .contents("user2 test contents")
                        .startTime(LocalTime.now())
                        .mealTime(2)
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.saveAll(List.of(meal1, meal2));

        List<MealListResponse> all = mealService.findAll();
        assertEquals(2, all.size());
    }
}