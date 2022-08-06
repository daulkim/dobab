package com.du.dobab.controller;

import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MyPageApiController {

    private final MealService mealService;

    @GetMapping("/api/v1/mypage/meals/{userId}")
    public List<MealListResponse> myMealList(@RequestParam(name="type") String type,
                                             @RequestParam(name="size") int size,
                                             @RequestParam(name="page") int page,
                                             @PathVariable(name = "userId") String userId) {
        return mealService.findByUserId(size, page, type, userId);
    }
}
