package com.du.dobab.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Location {

    @Column(name = "SI_NAME")
    private String siName;

    @Column(name = "GU_NAME")
    private String guName;

    @Column(name = "DONG_NAME")
    private String dongName;

    @Builder
    public Location(String siName, String guName, String dongName) {
        this.siName = siName;
        this.guName= guName;
        this.dongName = dongName;
    }
}
