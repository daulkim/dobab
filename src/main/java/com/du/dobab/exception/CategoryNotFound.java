package com.du.dobab.exception;

public class CategoryNotFound extends CustomException {

    private static final String MESSAGE = "존재하지 않는 카테고리입니다.";

    public CategoryNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
