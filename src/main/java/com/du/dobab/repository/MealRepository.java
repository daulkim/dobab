package com.du.dobab.repository;

import com.du.dobab.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal, Long>, MealRepositoryCustom {
}
