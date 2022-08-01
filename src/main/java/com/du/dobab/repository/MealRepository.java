package com.du.dobab.repository;

import com.du.dobab.domain.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long>, MealRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="3000")})
    Optional<Meal> findById(Long id);
    Optional<Meal> findByPartyId(Long partyId);
}
