package com.du.dobab.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(name = "PARTY")
public class Party {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PARTY_ID")
    private Long id;

    @Column(name = "USER_ID")
    private String userId;

    @OneToOne(mappedBy = "party")
    private Meal meal;

    @Builder
    public Party(String userId, Meal meal) {
        this.userId = userId;
        this.meal = meal;
    }
}
