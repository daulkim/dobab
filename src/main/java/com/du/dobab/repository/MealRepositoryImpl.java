package com.du.dobab.repository;

import com.du.dobab.domain.Meal;
import com.du.dobab.dto.request.MealSearch;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.du.dobab.domain.QMeal.meal;

@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Meal> getList(MealSearch mealSearch) {
        return jpaQueryFactory.selectFrom(meal)
                                .limit(mealSearch.getSize())
                                .offset(mealSearch.getOffset())
                                .orderBy(meal.id.desc())
                                .fetch();
    }
}
