package com.du.dobab.service;

import com.du.dobab.domain.MealRepository;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.dto.response.MealResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MealService {

    private final MealRepository mealRepository;

    public void save(MealSave mealSave) {
        mealRepository.save(mealSave.toEntity());
    }

    public MealResponse findById(Long id) {
        return mealRepository.findById(id).map(MealResponse::new)
                             .orElseThrow(() -> new IllegalArgumentException("존재하지않는 아이디입니다."));
    }

    public List<MealListResponse> findAll() {
        return mealRepository.findAll()
                            .stream()
                            .filter(m -> m.isOpened()).map(MealListResponse::new)
                            .collect(Collectors.toList());
    }
}
