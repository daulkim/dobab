package com.du.dobab.exception;

public class MealNotFound extends CustomException {

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public MealNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
