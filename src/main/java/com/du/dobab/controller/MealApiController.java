package com.du.dobab.controller;

import com.du.dobab.dto.request.MealEdit;
import com.du.dobab.dto.request.MealSave;
import com.du.dobab.dto.request.MealSearch;
import com.du.dobab.dto.response.MealListResponse;
import com.du.dobab.dto.response.MealResponse;
import com.du.dobab.service.MealService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class MealApiController {

    private final MealService mealService;

    @PostMapping("/api/v1/meals")
    public void registration(@RequestBody @Valid MealSave mealSave) {
        mealSave.validTime();
        mealService.save(mealSave);
    }

    @GetMapping("/api/v1/meals/{mealId}")
    public MealResponse findById(@PathVariable(name="mealId") Long id) {
        return mealService.findById(id);
    }

    @GetMapping("/api/v1/meals")
    public List<MealListResponse> list(@ModelAttribute MealSearch mealSearch) {
        return mealService.findAll(mealSearch);
    }

    @PatchMapping("/api/v1/meals/{mealId}")
    public void edit(@PathVariable(name="mealId") Long id, @RequestBody @Valid MealEdit mealEdit) {
        mealService.edit(id, mealEdit);
    }

    @DeleteMapping("/api/v1/meals/{mealId}")
    public void delete(@PathVariable(name="mealId") Long id) {
        mealService.delete(id);
    }
}
