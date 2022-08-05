package com.du.dobab.repository;

import com.du.dobab.domain.Meal;
import com.du.dobab.dto.MealStatus;
import com.du.dobab.dto.request.MealSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import static com.du.dobab.domain.QMeal.meal;
import static com.du.dobab.domain.QParty.party;

@RequiredArgsConstructor
public class MealRepositoryImpl implements MealRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Meal> getOpenList(MealSearch mealSearch) {
        return jpaQueryFactory.selectFrom(meal)
                                .where(meal.status.eq(MealStatus.OPEN))
                                .limit(mealSearch.getSize())
                                .offset(mealSearch.getOffset())
                                .orderBy(meal.id.desc())
                                .fetch();
    }

    @Override
    public int cntJoinMealByUserIdAndTime(String userId, Meal requestMeal) {

        return jpaQueryFactory.select(meal.id)
                                .from(meal)
                                .leftJoin((meal.party), party)
                                .where(meal.userId.eq(userId).or(party.userId.eq(userId)),
                                        meal.startDatetime.lt(requestMeal.getEndDatetime()),
                                        meal.endDatetime.gt(requestMeal.getStartDatetime())
                                )
                                .fetch().size();
    }

    @Override
    public List<Meal> getMyList(int size, int page, String type, String userId) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        if(StringUtils.equals(type, "meal")) booleanBuilder.and(meal.userId.eq(userId));
        if(StringUtils.equals(type, "party")) booleanBuilder.and(party.userId.eq(userId));
        if(StringUtils.equals(type, "all")) booleanBuilder.and(meal.userId.eq(userId).or(party.userId.eq(userId)));
        return jpaQueryFactory.selectFrom(meal)
                                .leftJoin((meal.party), party)
                                .where(booleanBuilder)
                                .limit(size)
                                .offset((page-1)*size)
                                .orderBy(meal.startDatetime.desc())
                                .fetch();
    }
}
