package com.du.dobab.service;

import com.du.dobab.domain.Meal;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.MealEdit;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.dto.request.MealSearch;
import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.dto.response.MealResponse;
import com.du.dobab.exception.MealNotFound;
import com.du.dobab.repository.MealRepository;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    @DisplayName("단건 조회")
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
    @DisplayName("1 페이지 조회")
    public void paging_list_succ() {

        List<Meal> meals = IntStream.range(0, 30)
                                    .mapToObj(i -> Meal.builder()
                                            .userId("user " + i)
                                            .title("test title " + i)
                                            .contents("test contents " +i)
                                            .startTime(LocalTime.now())
                                            .mealTime(2)
                                            .status(MealStatus.OPEN)
                                            .build())
                                    .collect(Collectors.toList());
        mealRepository.saveAll(meals);

        MealSearch mealSearch = MealSearch.builder()
                .build();
        List<MealListResponse> pageOne = mealService.findAll(mealSearch);
        assertEquals(10, pageOne.size());
        assertEquals(10, mealSearch.getSize());
        assertEquals(1, mealSearch.getPage());
        assertEquals(0, mealSearch.getOffset());
        assertEquals("user 29", pageOne.get(0).getUserId());

    }

    @Test
    @DisplayName("글 수정")
    public void edit_succ() {

        Meal meal = Meal.builder()
                        .userId("user")
                        .title("test title")
                        .contents("test contents")
                        .startTime(LocalTime.now())
                        .mealTime(2)
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        MealEdit mealEdit = MealEdit.builder()
                                    .title("edit title")
                                    .contents("edit contents")
                                    .build();
        mealService.edit(meal.getId(), mealEdit);

        Meal editedMeal = mealRepository.findById(meal.getId())
                                        .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id: " + meal.getId()));

        assertEquals("edit title", editedMeal.getTitle());
        assertEquals("edit contents", editedMeal.getContents());

    }

    @Test
    @DisplayName("글 삭제")
    public void delete_succ() {

        Meal meal = Meal.builder()
                .userId("user")
                .title("test title")
                .contents("test contents")
                .startTime(LocalTime.now())
                .mealTime(2)
                .status(MealStatus.OPEN)
                .build();
        mealRepository.save(meal);

        MealEdit mealEdit = MealEdit.builder()
                .title("edit title")
                .contents("edit contents")
                .build();
        mealService.edit(meal.getId(), mealEdit);

        Meal editedMeal = mealRepository.findById(meal.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다. id: " + meal.getId()));

        assertEquals("edit title", editedMeal.getTitle());
        assertEquals("edit contents", editedMeal.getContents());

    }

    @Test
    @DisplayName("단건 조회 - 글이 존재 하지 않는 경우")
    public void get_exception() {

        Meal meal = Meal.builder()
                        .userId("user")
                        .title("test title")
                        .contents("test contents")
                        .startTime(LocalTime.now())
                        .mealTime(2)
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        assertThrows(MealNotFound.class, () -> {
                        mealService.findById(meal.getId() + 1L);
                    });
    }

    @Test
    @DisplayName("글 수정 - 글이 존재 하지 않는 경우")
    public void edit_exception() {

        Meal meal = Meal.builder()
                        .userId("user")
                        .title("test title")
                        .contents("test contents")
                        .startTime(LocalTime.now())
                        .mealTime(2)
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        MealEdit mealEdit = MealEdit.builder()
                                    .title("edit title")
                                    .contents("edit contents")
                                    .build();

        assertThrows(MealNotFound.class, () -> {
                        mealService.edit(meal.getId() + 1L, mealEdit);
                    });

    }
}