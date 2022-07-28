package com.du.dobab.service;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.PartySave;
import com.du.dobab.exception.PartyNotFound;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}