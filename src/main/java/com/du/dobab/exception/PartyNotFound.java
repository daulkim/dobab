package com.du.dobab.exception;

public class PartyNotFound extends CustomException {

    private static final String MESSAGE = "참여 정보가 존재하지 않습니다.";

    public PartyNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
