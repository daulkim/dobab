package com.du.dobab.repository;

import com.du.dobab.domain.Meal;
import com.du.dobab.dto.request.MealSearch;

import java.util.List;

public interface MealRepositoryCustom {

    List<Meal> getOpenList(MealSearch mealSearch);
    int cntJoinMealByUserIdAndTime(String userId, Meal requestMeal);
    List<Meal> getMyList(int size, int page, String type, String userId);

}
