package com.du.dobab.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Location {

    @Column(name = "SI_NAME")
    private String siName;

    @Column(name = "GU_NAME")
    private String guName;

    @Column(name = "DONG_NAME")
    private String dongName;
}
