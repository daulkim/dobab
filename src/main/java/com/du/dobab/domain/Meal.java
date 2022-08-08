package com.du.dobab.domain;

import com.du.dobab.dto.MealStatus;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity @Table(name = "MEAL")
public class Meal {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MEAL_ID")
    private Long id;

    @Column(name="USER_ID")
    private String userId;

    @Column(name="TITLE")
    private String title;

    @Column(name="CONTENTS")
    private String contents;

    @Column(name="START_DATETIME")
    private LocalDateTime startDatetime;

    @Column(name="END_DATETIME")
    private LocalDateTime endDatetime;

    @Column(name="STATUS")
    @Enumerated(value = EnumType.STRING)
    private MealStatus status;

    @Embedded
    private Location location;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "party_id")
    private Party party;

    public Meal(Long id) {
        this.id = id;
    }

    @Builder
    public Meal(String userId, String title, String contents,
                LocalDateTime startDatetime, LocalDateTime endDatetime,
                MealStatus status, Location location) {
        this.userId = userId;
        this.title = title;
        this.contents = contents;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.status = status;
        this.location = location;
    }

    public boolean isOpened() {
        return this.status == MealStatus.OPEN;
    }

    public void edit(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public void join(Party party) {
        this.setParty(party);
        this.status = MealStatus.FULL;
    }

    public void setParty(Party party) {
        this.party = party;
        this.party.setMeal(this);
    }

    public void leave() {
        this.party = null;
    }

    public void delete() {
        this.status = MealStatus.DELETE;
    }
}
