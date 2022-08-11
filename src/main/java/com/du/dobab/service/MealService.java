package com.du.dobab.service;

import com.du.dobab.common.exception.CustomValidation;
import com.du.dobab.domain.Category;
import com.du.dobab.domain.Meal;
import com.du.dobab.dto.request.MealEdit;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.dto.request.MealSearch;
import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.dto.response.MealResponse;
import com.du.dobab.exception.CategoryNotFound;
import com.du.dobab.exception.InvalidMealException;
import com.du.dobab.exception.MealNotFound;
import com.du.dobab.repository.CategoryRepository;
import com.du.dobab.repository.MealRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void save(MealSave mealSave) {
        boolean isAvailableTimeOfUser = mealRepository.cntJoinMealByUserIdAndTime(mealSave.getUserId(), mealSave.toEntity()) == 0;
        Category category = categoryRepository.findById(mealSave.getCategoryId()).orElseThrow(CategoryNotFound::new);

        if(isAvailableTimeOfUser) {
            Meal meal = mealRepository.save(mealSave.toEntity());
            meal.setCategory(category);
        }
        else {
            throw new InvalidMealException(CustomValidation.INVALID_MEAL_SAVE);
        }
    }

    public MealResponse findById(Long id) {
        return mealRepository.findById(id).map(MealResponse::new)
                            .orElseThrow(MealNotFound::new);
    }

    public List<MealListResponse> findAll(MealSearch mealSearch) {
        return mealRepository.getOpenList(mealSearch)
                            .stream()
                            .map(MealListResponse::new)
                            .collect(Collectors.toList());
    }

    @Transactional
    public void edit(Long id, MealEdit mealEdit) {
        Meal meal = mealRepository.findById(id)
                                    .orElseThrow(MealNotFound::new);
        meal.edit(mealEdit.getTitle(), mealEdit.getContents());
    }

    public List<MealListResponse> findByUserId(int size, int page, String type, String userId) {
        return mealRepository.getMyList(size, page, type, userId)
                            .stream()
                            .map(MealListResponse::new)
                            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Meal meal = mealRepository.findById(id)
                                    .orElseThrow(MealNotFound::new);
        if(meal.isOpened()) {
            meal.delete();
        }
        else {
            throw new InvalidMealException(CustomValidation.INVALID_MEAL_DELETE_STATUS);
        }
    }
}
