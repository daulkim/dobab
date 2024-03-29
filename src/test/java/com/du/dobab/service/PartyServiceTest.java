package com.du.dobab.service;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.PartySave;
import com.du.dobab.exception.InvalidMealException;
import com.du.dobab.exception.PartyNotFound;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class PartyServiceTest {

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PartyService partyService;

    @Autowired
    private PartyRepository partyRepository;

    @AfterEach
    public void clean() {
        mealRepository.deleteAll();
        partyRepository.deleteAll();
    }

    @Test
    @DisplayName("party 등록 성공")
    public void save_succ() {
        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
                                            .status(MealStatus.OPEN)
                                            .build());
        PartySave partySave = PartySave.builder()
                                        .userId("user2")
                                        .mealId(meal.getId())
                                        .build();
        partyService.save(partySave);

        Party savedParty = partyRepository.findAll().get(0);
        assertEquals(1L, mealRepository.count());
        assertEquals(1L, partyRepository.count());
        assertEquals(meal.getUserId(), savedParty.getMeal().getUserId());
        assertEquals("user2", savedParty.getUserId());
    }

    @Transactional
    @Test
    @DisplayName("party 삭제 실패")
    public void delete_exception() {
        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .status(MealStatus.OPEN)
                                            .build());
        Party party = Party.builder()
                            .userId("user2")
                            .meal(meal)
                            .build();

        meal.join(party);

        long partyId = partyRepository.findAll().get(0).getId();

        Assertions.assertThrows(PartyNotFound.class, () -> {
            partyService.delete(partyId + 1L);
        });
    }

    @Transactional
    @Test
    @DisplayName("party 삭제 성공")
    public void delete_succ() {
        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .status(MealStatus.OPEN)
                                            .build());
        Party party = Party.builder()
                            .userId("user2")
                            .meal(meal)
                            .build();

        meal.join(party);
        partyService.delete(partyRepository.findAll().get(0).getId());

        int size = partyRepository.findAll().size();
        assertEquals(0, size);
        assertEquals(null, meal.getParty());
    }

    @Test
    @DisplayName("party 등록 - 식사 상태가 OPEN이 아닌  경우")
    public void save_fail() {
        Meal meal = mealRepository.save(Meal.builder()
                                    .userId("user1")
                                    .startDatetime(LocalDateTime.now().plusHours(1))
                                    .endDatetime(LocalDateTime.now().plusHours(2))
                                    .status(MealStatus.TIMEOUT)
                                    .build());
        PartySave partySave = PartySave.builder()
                                        .userId("user2")
                                        .mealId(meal.getId())
                                        .build();
        Assertions.assertThrows(InvalidMealException.class, () -> {
            partyService.save(partySave);
        });
    }

    @Test
    @DisplayName("party 등록 - 이미 같은 시간대에 다른 meal에 참여한 경우")
    public void save_fail2() {
        Meal meal1 = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(5))
                                            .status(MealStatus.OPEN)
                                            .build());
        Meal meal2 = mealRepository.save(Meal.builder()
                                            .userId("user2")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
                                            .status(MealStatus.OPEN)
                                            .build());
        PartySave partySave1 = PartySave.builder()
                                        .userId("user3")
                                        .mealId(meal1.getId())
                                        .build();
        partyService.save(partySave1);

        PartySave partySave2 = PartySave.builder()
                                        .userId("user3")
                                        .mealId(meal2.getId())
                                        .build();

        Assertions.assertThrows(InvalidMealException.class, () -> {
            partyService.save(partySave2);
        });
    }

    @Test
    @DisplayName("party 등록 성공 - 식사에 참여 했지만 시간이 겹치지 않는 또 다른 식사에 참여 요청")
    public void save_succ2() {
        Meal meal1 = mealRepository.save(Meal.builder()
                                    .userId("user1")
                                    .startDatetime(LocalDateTime.now().plusHours(1))
                                    .endDatetime(LocalDateTime.now().plusHours(2))
                                    .status(MealStatus.OPEN)
                                    .build());
        Meal meal2 = mealRepository.save(Meal.builder()
                                    .userId("user2")
                                    .startDatetime(LocalDateTime.now().plusHours(2))
                                    .endDatetime(LocalDateTime.now().plusHours(3))
                                    .status(MealStatus.OPEN)
                                    .build());
        PartySave partySave1 = PartySave.builder()
                                        .userId("user3")
                                        .mealId(meal1.getId())
                                        .build();
        PartySave partySave2 = PartySave.builder()
                                        .userId("user3")
                                        .mealId(meal2.getId())
                                        .build();
        partyService.save(partySave1);
        partyService.save(partySave2);

        List<Party> all = partyRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("party 등록 성공 - 겹치는 시간대의 서로 다른 식사에 두 명의 유저가 서로 다른 식사에 참여 요청")
    public void save_succ3() {
        Meal meal1 = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(5))
                                            .status(MealStatus.OPEN)
                                            .build());
        Meal meal2 = mealRepository.save(Meal.builder()
                                            .userId("user2")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(3))
                                            .status(MealStatus.OPEN)
                                            .build());
        PartySave partySave1 = PartySave.builder()
                                        .userId("user3")
                                        .mealId(meal1.getId())
                                        .build();
        PartySave partySave2 = PartySave.builder()
                                        .userId("user4")
                                        .mealId(meal2.getId())
                                        .build();
        partyService.save(partySave1);
        partyService.save(partySave2);

        List<Party> all = partyRepository.findAll();
        assertEquals(2, all.size());
    }

    @Test
    @DisplayName("party 등록 실패 - 한 유저가 등록한 식사와 겹치는 시간의 식사 참여 요청")
    public void save_fail3() {
        mealRepository.save(Meal.builder()
                                .userId("user1")
                                .startDatetime(LocalDateTime.now().plusHours(1))
                                .endDatetime(LocalDateTime.now().plusHours(5))
                                .status(MealStatus.OPEN)
                                .build());
        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user2")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(3))
                                            .status(MealStatus.OPEN)
                                            .build());
        PartySave partySave1 = PartySave.builder()
                                        .userId("user1")
                                        .mealId(meal.getId())
                                        .build();

        assertThrows(InvalidMealException.class, () -> {
            partyService.save(partySave1);
        });
    }

    @Transactional
    @Test
    @DisplayName("party 등록 성공 - 한 유저가 등록한 식사와 겹치는 않는 시간의 식사 참여 요청")
    public void save_succ4() {
        mealRepository.save(Meal.builder()
                                .userId("user1")
                                .startDatetime(LocalDateTime.now().plusHours(1))
                                .endDatetime(LocalDateTime.now().plusHours(2))
                                .status(MealStatus.OPEN)
                                .build());
        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user2")
                                            .startDatetime(LocalDateTime.now().plusHours(2))
                                            .endDatetime(LocalDateTime.now().plusHours(3))
                                            .status(MealStatus.OPEN)
                                            .build());
        PartySave partySave1 = PartySave.builder()
                                        .userId("user1")
                                        .mealId(meal.getId())
                                        .build();

        partyService.save(partySave1);
        List<Party> allParty = partyRepository.findAll();
        List<Meal> allMeal = mealRepository.findAll();
        assertEquals(1, allParty.size());
        assertEquals("user1", allParty.get(0).getUserId());
        assertEquals("user1", allMeal.get(1).getParty().getUserId());
    }

    @Test
    public void temp_test() {
        System.out.println(LocalDateTime.of(2022, 8,4, 00,00).minusMinutes(10));
        System.out.println(LocalTime.of(00,8).isAfter(LocalTime.of(00,8).minusMinutes(10)));
    }
}