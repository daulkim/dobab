package com.du.dobab.controller;

import com.du.dobab.domain.Meal;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.MealSave;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MealApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MealRepository mealRepository;

    @AfterEach
    public void clean() {
        mealRepository.deleteAll();
    }

    @DisplayName("save 요청 시 파라미터 validation 실패")
    @Test
    public void save_fail() throws Exception {

        MealSave mealSave = MealSave.builder()
                                    .userId("user")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startTime(LocalTime.now())
                                    .mealTime(0)
                                    .build();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = objectMapper.writeValueAsString(mealSave);

        mockMvc.perform(post("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.mealTime").value("소요시간은 한시간 보다 커야합니다."))
                .andDo(print());
    }

    @DisplayName("save 요청 시 파라미터 validation 성공")
    @Test
    public void save_succ() throws Exception {

        MealSave mealSave = MealSave.builder()
                                    .userId("user")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startTime(LocalTime.now())
                                    .mealTime(1)
                                    .build();
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = objectMapper.writeValueAsString(mealSave);

        mockMvc.perform(post("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("get 요청 시 성공")
    @Test
    public void get_succ() throws Exception {

        MealSave mealSave = MealSave.builder()
                                    .userId("user")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startTime(LocalTime.now())
                                    .mealTime(1)
                                    .build();
        Meal savedMeal = mealRepository.save(mealSave.toEntity());

        mockMvc.perform(get("/api/v1/meals/{mealId}", savedMeal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value("user"))
                .andExpect(jsonPath("$.mealTime").value(1))
                .andExpect(jsonPath("$.contents").value("테스트 글입니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("get all 요청 시 성공")
    public void list_succ() throws Exception {
        LocalTime now = LocalTime.now();

        Meal meal1 = Meal.builder()
                        .userId("user1")
                        .title("user1 test")
                        .contents("user1 test contents")
                        .startTime(now)
                        .mealTime(3)
                        .status(MealStatus.OPEN)
                        .build();
        Meal meal2 = Meal.builder()
                        .userId("user2")
                        .title("user2 test")
                        .contents("user2 test contents")
                        .startTime(now)
                        .mealTime(2)
                        .status(MealStatus.OPEN)
                        .build();

        mealRepository.saveAll(List.of(meal1, meal2));

        mockMvc.perform(get("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(2)))
                .andExpect(jsonPath("$[0].userId").value("user1"))
                .andExpect(jsonPath("$[0].title").value("user1 test"))
                .andExpect(jsonPath("$[0].startTime").value(now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .andExpect(jsonPath("$[1].userId").value("user2"))
                .andExpect(jsonPath("$[1].title").value("user2 test"))
                .andExpect(jsonPath("$[1].startTime").value(now.format(DateTimeFormatter.ofPattern("HH:mm:ss"))))
                .andDo(print());
    }
}