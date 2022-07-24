package com.du.dobab.dto.request;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class MealEdit {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String contents;

    @Builder
    public MealEdit(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }
}
