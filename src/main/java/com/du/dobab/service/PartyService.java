package com.du.dobab.service;

import com.du.dobab.common.exception.CustomValidation;
import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import com.du.dobab.exception.InvalidMealException;
import com.du.dobab.dto.request.PartySave;
import com.du.dobab.exception.MealNotFound;
import com.du.dobab.exception.PartyNotFound;
import com.du.dobab.repository.MealRepository;
import com.du.dobab.repository.PartyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PartyService {

    private final PartyRepository partyRepository;
    private final MealRepository mealRepository;

    @Transactional
    public void save(PartySave partySave) {
        Meal requestMeal = mealRepository.findById(partySave.getMealId()).orElseThrow(MealNotFound::new);
        boolean isOpenedMeal = requestMeal.isOpened();
        boolean isAvailableTimeOfUser = mealRepository.cntJoinMealByUserIdAndTime(partySave.getUserId(), requestMeal) == 0;
        if(isOpenedMeal && isAvailableTimeOfUser) {
            Party party = partySave.toEntity(requestMeal);
            requestMeal.join(party);
        }
        else {
            throw new InvalidMealException(CustomValidation.INVALID_JOIN_STATUS);
        }
    }

    @Transactional
    public void delete(Long id) {
        Party savedParty = partyRepository.findById(id).orElseThrow(PartyNotFound::new);
        Meal savedMeal = mealRepository.findByPartyId(savedParty.getId()).orElseThrow(MealNotFound::new);
        savedMeal.leave();
    }
}
