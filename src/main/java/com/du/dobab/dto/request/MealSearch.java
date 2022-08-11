package com.du.dobab.dto.request;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Builder
@Getter
public class MealSearch {

    private static final int MAX_SIZE = 2000;

    @Builder.Default
    private Integer page = 1;

    @Builder.Default
    private Integer size = 12;

    public long getOffset() {
        return (long) (max(1, this.page) - 1) * min(this.size, MAX_SIZE);
    }
}
