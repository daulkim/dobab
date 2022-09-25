package com.du.dobab.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role {

    ADMIN("ADMIN", "관리자"),
    USER("USER", "사용자");

    private final String key;
    private final String title;
}
