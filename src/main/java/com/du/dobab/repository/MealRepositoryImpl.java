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

    @Override
    public int cntJoinMealByUserIdAndTime(String userId, Meal requestMeal) {
        return jpaQueryFactory.selectFrom(meal)
                                .where(meal.party.userId.eq(userId),
                                        meal.startDatetime.lt(requestMeal.getEndDatetime()),
                                        meal.endDatetime.gt(requestMeal.getStartDatetime()))
                                .fetch().size();
    }
}
