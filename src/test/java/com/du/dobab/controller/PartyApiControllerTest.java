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
        partyRepository.save(party);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/party/{partyId}", party.getId()+1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value("404"))
                .andExpect(jsonPath("$.message").value("참여 정보가 존재하지 않습니다."))
                .andDo(print());
    }

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
        partyRepository.save(party);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/party/{partyId}", party.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}