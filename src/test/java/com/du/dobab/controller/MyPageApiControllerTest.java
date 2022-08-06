package com.du.dobab.controller;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MyPageApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PartyRepository partyRepository;

    @AfterEach
    public void clean() {
        partyRepository.deleteAll();
        mealRepository.deleteAll();
    }

    @DisplayName("유저가 제안한 식사리스트 요청 성공")
    @Test
    public void myMealList_meal_succ() throws Exception {

        List<Meal> meals = IntStream.range(0, 30)
                                    .mapToObj(i -> Meal.builder()
                                            .userId("user" + i%3)
                                            .title("test title " + i)
                                            .contents("test contents " +i)
                                            .startDatetime(LocalDateTime.now().plusHours(i))
                                            .endDatetime(LocalDateTime.now().plusHours(i+1))
                                            .status(MealStatus.OPEN)
                                            .build())
                                    .collect(Collectors.toList());
        mealRepository.saveAll(meals);
        meals.forEach(m -> {m.setParty(Party.builder()
                                            .userId("party user")
                                            .meal(m)
                                            .build());}
            );

        mockMvc.perform(get("/api/v1/mypage/meals/{userId}", "user1")
                        .param("type","meal")
                        .param("size","10")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].title").value("test title 28"))
                .andExpect(jsonPath("$[9].userId").value("user1"))
                .andExpect(jsonPath("$[9].title").value("test title 1"))
                .andDo(print());
    }

    @Transactional
    @DisplayName("유저가 참여한 식사리스트 요청 성공")
    @Test
    public void myMealList_party_succ() throws Exception {

        List<Meal> meals = IntStream.range(0, 30)
                                    .mapToObj(i -> Meal.builder()
                                            .userId("user" + i%3)
                                            .title("test title " + i)
                                            .contents("test contents " +i)
                                            .startDatetime(LocalDateTime.now().plusHours(i))
                                            .endDatetime(LocalDateTime.now().plusHours(i+1))
                                            .status(MealStatus.OPEN)
                                            .build())
                                    .collect(Collectors.toList());
        mealRepository.saveAll(meals);
        meals.forEach(m -> {m.setParty(Party.builder()
                                            .userId("party user")
                                            .meal(m)
                                            .build());}
             );

        mockMvc.perform(get("/api/v1/mypage/meals/{userId}", "party user")
                        .param("type","party")
                        .param("size","10")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user2"))
                .andExpect(jsonPath("$[0].title").value("test title 29"))
                .andExpect(jsonPath("$[1].userId").value("user1"))
                .andExpect(jsonPath("$[1].title").value("test title 28"))
                .andDo(print());
    }

    @Transactional
    @DisplayName("유저가 제안 및 참여한 식사리스트 요청 성공")
    @Test
    public void myMealList_meal_and_party_succ() throws Exception {

        List<Meal> meals = IntStream.range(0, 30)
                                    .mapToObj(i -> Meal.builder()
                                            .userId("user" + i%3)
                                            .title("test title " + i)
                                            .contents("test contents " +i)
                                            .startDatetime(LocalDateTime.now().plusHours(i))
                                            .endDatetime(LocalDateTime.now().plusHours(i+1))
                                            .status(MealStatus.OPEN)
                                            .build())
                                    .collect(Collectors.toList());
        mealRepository.saveAll(meals);
        meals.forEach(m -> {m.setParty(Party.builder()
                                            .userId(m.getUserId().equals("user0")?"user1":"user0")
                                            .meal(m)
                                            .build());}
                );

        mockMvc.perform(get("/api/v1/mypage/meals/{userId}", "user1")
                        .param("type","all")
                        .param("size","10")
                        .param("page","1")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].title").value("test title 28"))
                .andExpect(jsonPath("$[1].userId").value("user0"))
                .andExpect(jsonPath("$[1].title").value("test title 27"))
                .andDo(print());
    }
}