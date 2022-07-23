package com.du.dobab.repository;

import com.du.dobab.domain.Meal;
import com.du.dobab.dto.request.MealSearch;

import java.util.List;

public interface MealRepositoryCustom {

    List<Meal> getList(MealSearch mealSearch);
}
