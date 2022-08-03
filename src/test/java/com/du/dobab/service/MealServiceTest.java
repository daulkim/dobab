package com.du.dobab.service;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.MealEdit;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.dto.request.MealSearch;
import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.dto.response.MealResponse;
import com.du.dobab.exception.InvalidMealException;
import com.du.dobab.exception.MealNotFound;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
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

    @Autowired
    private PartyRepository partyRepository;

    @AfterEach
    public void clean() {
        partyRepository.deleteAll();
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
                                    .startDatetime(LocalDateTime.now().plusHours(1))
                                    .mealTime(3)
                                    .contents(content)
                                    .build();

        mealService.save(mealSave);

        Meal savedMeal = mealRepository.findAll().get(0);
        assertEquals(1L, mealRepository.count());
        assertEquals(savedMeal.getUserId(), userId);
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
                        .startDatetime(LocalDateTime.now().plusHours(1))
                        .endDatetime(LocalDateTime.now().plusHours(2))
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        MealResponse savedMeal = mealService.findById(meal.getId());

        assertEquals(1L, mealRepository.count());
        assertEquals(savedMeal.getUserId(), userId);
        assertEquals(savedMeal.getMealTime(), 1);
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
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
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

    @Transactional
    @Test
    @DisplayName("글 수정")
    public void edit_succ() {

        Meal meal = Meal.builder()
                        .userId("user")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now())
                        .endDatetime(LocalDateTime.now().plusHours(2))
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
                        .startDatetime(LocalDateTime.now())
                        .endDatetime(LocalDateTime.now().plusHours(2))
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        assertThrows(MealNotFound.class, () -> {
                        mealService.findById(meal.getId() + 1L);
                    });
    }

    @Test
    @DisplayName("글 수정 실패 - 글이 존재 하지 않는 경우")
    public void edit_exception() {

        Meal meal = Meal.builder()
                        .userId("user")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now().plusHours(1))
                        .endDatetime(LocalDateTime.now().plusHours(2))
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

    @Transactional
    @Test
    @DisplayName("글 등록 실패 - 한 유저가 참여한 식사와 겹치는 시간의 식사 등록 요청")
    public void save_fail() {

        Meal meal = Meal.builder()
                        .userId("user1")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now().plusHours(1))
                        .endDatetime(LocalDateTime.now().plusHours(2))
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);
        Party party = Party.builder()
                            .userId("user2")
                            .meal(meal)
                            .build();
        partyRepository.save(party);
        meal.join(party);

        MealSave mealSave = MealSave.builder()
                                    .userId("user2")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startDatetime(LocalDateTime.now().plusHours(1))
                                    .mealTime(1)
                                    .build();

        assertThrows(InvalidMealException.class, () -> {
            mealService.save(mealSave);
        });
    }

    @Transactional
    @Test
    @DisplayName("글 등록 성공 - 한 유저의 참여 식사와 등록 식사의 시간이 겹치지 않는 경우")
    public void save_succ2() {

        Meal meal = Meal.builder()
                        .userId("user1")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now().plusHours(1))
                        .endDatetime(LocalDateTime.now().plusHours(2))
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);
        Party party = Party.builder()
                            .userId("user2")
                            .meal(meal)
                            .build();
        partyRepository.save(party);
        meal.join(party);

        MealSave mealSave = MealSave.builder()
                                    .userId("user2")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startDatetime(LocalDateTime.now().plusHours(2))
                                    .mealTime(1)
                                    .build();

        mealService.save(mealSave);
        List<Meal> all = mealRepository.findAll();
        assertEquals(2, all.size());
        assertEquals("user2", all.get(1).getUserId());
        assertEquals("user2", all.get(0).getParty().getUserId());

    }
}