package com.du.dobab.controller;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.PartySave;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class PartyApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private PartyRepository partyRepository;

    @AfterEach
    public void clean() {
        mealRepository.deleteAll();
        partyRepository.deleteAll();
    }

    @Test
    @DisplayName("join 요청 시 성공")
    public void join_succ() throws Exception {

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
        objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/party")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(partySave))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("leave 요청 시 실패")
    public void leave_fail() throws Exception {

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
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/party/{partyId}", partyId + 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("참여 정보가 존재하지 않습니다."))
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("leave 요청 시 성공")
    public void leave_succ() throws Exception {

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
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/party/{partyId}", partyId))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("join 요청 시 실패 - 한 유저가 겹치는 식사시간의 식사 참여 요청")
    public void join_fail() throws Exception {

        Meal meal1 = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
                                            .status(MealStatus.OPEN)
                                            .build());

        Meal meal2 = mealRepository.save(Meal.builder()
                                            .userId("user2")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
                                            .status(MealStatus.OPEN)
                                            .build());

        Party party = Party.builder()
                            .userId("user3")
                            .meal(meal1)
                            .build();
        meal1.join(party);

        PartySave partySave = PartySave.builder()
                                        .userId("user3")
                                        .mealId(meal2.getId())
                                        .build();

        objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/party")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partySave))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.joinStatus").value("해당 식사에 참여할 수 없습니다."))
                .andDo(print());
    }

    @Transactional
    @Test
    @DisplayName("join 요청 시 성공 - 다른 유저가 겹치는 식사시간의 식사 참여 요청")
    public void join_succ2() throws Exception {

        Meal meal1 = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
                                            .status(MealStatus.OPEN)
                                            .build());

        Meal meal2 = mealRepository.save(Meal.builder()
                                    .userId("user2")
                                    .startDatetime(LocalDateTime.now().plusHours(1))
                                    .endDatetime(LocalDateTime.now().plusHours(2))
                                    .status(MealStatus.OPEN)
                                    .build());

        Party party = Party.builder()
                            .userId("user3")
                            .meal(meal1)
                            .build();
        meal1.join(party);

        PartySave partySave = PartySave.builder()
                                        .userId("user4")
                                        .mealId(meal2.getId())
                                        .build();

        objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/party")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partySave))
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("join 요청 시 실패 - 이미 참여가 완료된 요청")
    public void join_fail2() throws Exception {

        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user1")
                                            .startDatetime(LocalDateTime.now().plusHours(1))
                                            .endDatetime(LocalDateTime.now().plusHours(2))
                                            .status(MealStatus.FULL)
                                            .build());

        PartySave partySave = PartySave.builder()
                                        .userId("user2")
                                        .mealId(meal.getId())
                                        .build();

        objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/party")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partySave))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.joinStatus").value("해당 식사에 참여할 수 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("join 요청 시 실패 - 한 유저가 등록한 식사와 겹치는 시간의 식사 참여 요청")
    public void join_fail3() throws Exception {

        mealRepository.save(Meal.builder()
                                .userId("user1")
                                .startDatetime(LocalDateTime.now().plusHours(1))
                                .endDatetime(LocalDateTime.now().plusHours(5))
                                .status(MealStatus.OPEN)
                                .build());

        Meal meal = mealRepository.save(Meal.builder()
                                            .userId("user2")
                                            .startDatetime(LocalDateTime.now().plusHours(2))
                                            .endDatetime(LocalDateTime.now().plusHours(3))
                                            .status(MealStatus.OPEN)
                                            .build());

        PartySave partySave = PartySave.builder()
                                        .userId("user1")
                                        .mealId(meal.getId())
                                        .build();

        objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/party")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partySave))
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.joinStatus").value("해당 식사에 참여할 수 없습니다."))
                .andDo(print());
    }

    @Test
    @DisplayName("join 요청 시 성공 - 한 유저가 등록한 식사와 겹치지 않는 시간의 식사 참여 요청")
    public void join_succ3() throws Exception {

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

        PartySave partySave = PartySave.builder()
                                        .userId("user1")
                                        .mealId(meal.getId())
                                        .build();

        objectMapper = new ObjectMapper();

        mockMvc.perform(post("/api/v1/party")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partySave))
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
}