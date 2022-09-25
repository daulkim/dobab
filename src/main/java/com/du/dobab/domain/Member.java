package com.du.dobab.domain;

import com.du.dobab.dto.Role;
import lombok.*;

import javax.persistence.*;

@ToString
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    private String email;

    private String username;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String email, String username, Role role) {
        this.email = email;
        this.username = username;
        this.role = role;
    }

    public Member update(String username) {
        this.username = username;
        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
