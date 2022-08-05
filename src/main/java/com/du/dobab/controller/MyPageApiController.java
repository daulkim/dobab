package com.du.dobab.controller;

import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyPageApiController {

    private final MealService mealService;

    @GetMapping("/api/v1/mypage/meal")
    public List<MealListResponse> myMealList(@PathVariable String type, int size, int page, String userId) {
        return mealService.findByUserId(size, page, type, userId);
    }
}
