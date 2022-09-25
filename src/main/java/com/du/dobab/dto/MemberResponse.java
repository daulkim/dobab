package com.du.dobab.dto;

import com.du.dobab.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberResponse {
    private String username;
    private Role role;

    @Builder
    public MemberResponse(Member entity) {
        this.username = entity.getUsername();
        this.role = entity.getRole();
    }
}
