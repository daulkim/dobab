package com.du.dobab.service;

import com.du.dobab.exception.CustomException;

public class AlreadyFull extends CustomException {

    private static final String MESSAGE = "이미 정원이 찬 식사입니다.";

    public AlreadyFull() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
