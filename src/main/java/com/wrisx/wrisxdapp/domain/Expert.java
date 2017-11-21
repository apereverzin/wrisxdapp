package com.wrisx.wrisxdapp.domain;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Table(name = "expert")
public class Expert {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String address;

    @NotNull
    private String name;

    @NotNull
    private String emailAddress;

    @NotNull
    private String keywords;

    @NotNull
    private String description;

    public Expert() {
        //
    }

    public Expert(String address, String name, String emailAddress,
                  String keywords, String description) {
        this.address = address;
        this.name = name;
        this.emailAddress = emailAddress;
        this.keywords = keywords;
        this.description = description;
    }
}
