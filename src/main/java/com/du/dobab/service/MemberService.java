package com.du.dobab.service;

import com.du.dobab.dto.MemberResponse;
import com.du.dobab.jwt.JwtTokenUtil;
import com.du.dobab.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @GetMapping("/login")
    public MemberResponse getMember(Authentication authentication) {
        String username = authentication.getName();
        log.info("username: {}, auth: {}", username, authentication.getAuthorities());
        return memberRepository.findByUsername(username)
                .map(MemberResponse::new)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디"));
    }
}
