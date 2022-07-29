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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
        assertEquals(savedParty.getUserId(), "user2");
    }

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
        partyRepository.save(party);

        Assertions.assertThrows(PartyNotFound.class, () -> {
            partyService.delete(party.getId()+1L);
        });
    }

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
        partyRepository.save(party);
        partyService.delete(party.getId());

        int size = partyRepository.findAll().size();
        Assertions.assertEquals(0, size);
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
}