package com.du.dobab.dto.request;

import com.du.dobab.domain.Meal;
import com.du.dobab.domain.Party;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PartySave {

    private String userId;
    private Long mealId;

    @Builder
    public PartySave(String userId, Long mealId) {
        this.userId = userId;
        this.mealId = mealId;
    }

    public Party toEntity() {
        return Party.builder()
                    .userId(userId)
                    .meal(new Meal(mealId))
                    .build();
    }
}
