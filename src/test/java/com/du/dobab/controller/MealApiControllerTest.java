package com.du.dobab.controller;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.MealEdit;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
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

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MealApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PartyRepository partyRepository;

    @AfterEach
    public void clean() {
        partyRepository.deleteAll();
        mealRepository.deleteAll();
    }

    @DisplayName("save 요청 시 파라미터 validation 실패")
    @Test
    public void save_fail() throws Exception {

        MealSave mealSave = MealSave.builder()
                                    .userId("user")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startDatetime(LocalDateTime.now().plusHours(1))
                                    .mealTime(0)
                                    .build();
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = objectMapper.writeValueAsString(mealSave);

        mockMvc.perform(post("/api/v1/meals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.mealTime").value("소요시간은 한 시간 이상이어야 합니다."))
                .andDo(print());
    }

    @DisplayName("save 요청 시 파라미터 validation 성공")
    @Test
    public void save_succ() throws Exception {

        MealSave mealSave = MealSave.builder()
                                    .userId("user")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startDatetime(LocalDateTime.now().plusHours(1))
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
                                    .startDatetime(LocalDateTime.now().plusHours(1))
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

        mockMvc.perform(get("/api/v1/meals?page=1&size=10")
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].userId").value("user 29"))
                .andExpect(jsonPath("$[9].userId").value("user 20"))
                .andDo(print());
    }

    @Test
    @DisplayName("edit 요청 시 성공")
    public void edit_succ() throws Exception {

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
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        mockMvc.perform(patch("/api/v1/meals/{mealId}", meal.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealEdit))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("get 요청 - 글이 존재하지 않는 경우")
    public void get_exception() throws Exception {

        Meal meal = Meal.builder()
                        .userId("user")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now().plusHours(1))
                        .endDatetime(LocalDateTime.now().plusHours(2))
                        .status(MealStatus.OPEN)
                        .build();
        mealRepository.save(meal);

        mockMvc.perform(get("/api/v1/meals/{mealId}", meal.getId() + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("edit 요청 - 글이 존재하지 않는 경우")
    public void edit_exception() throws Exception {

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
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

        mockMvc.perform(patch("/api/v1/meals/{mealId}", meal.getId() + 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mealEdit))
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @DisplayName("save 요청 - 잘못된 식사시간")
    @Test
    public void save_mealtime_exception() throws Exception {

        MealSave mealSave = MealSave.builder()
                                    .userId("user")
                                    .title("user test")
                                    .contents("테스트 글입니다.")
                                    .startDatetime(LocalDateTime.now())
                                    .mealTime(1)
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
                .andExpect(jsonPath("$.validation.startTime").value("식사 시작시간이 10분 보다 적게 남았습니다."))
                .andDo(print());
    }

    @Transactional
    @DisplayName("save 요청 실패- 한 유저가 참여한 식사와 겹치는 시간의 식사 등록 요청")
    @Test
    public void save_fail2() throws Exception {

        Meal meal = Meal.builder()
                        .userId("user1")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES))
                        .endDatetime(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES))
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
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = objectMapper.writeValueAsString(mealSave);

        mockMvc.perform(post("/api/v1/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.mealTime").value("해당 식사를 등록할 수 없습니다."))
                .andDo(print());
    }

    @Transactional
    @DisplayName("save 요청 성공- 한 유저가 참여한 식사와 겹치지 않는 시간의 식사 등록 요청")
    @Test
    public void save_succ2() throws Exception {

        Meal meal = Meal.builder()
                        .userId("user1")
                        .title("test title")
                        .contents("test contents")
                        .startDatetime(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.MINUTES))
                        .endDatetime(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.MINUTES))
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
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        String requestJson = objectMapper.writeValueAsString(mealSave);

        mockMvc.perform(post("/api/v1/meals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
}