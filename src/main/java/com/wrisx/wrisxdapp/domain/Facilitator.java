package com.wrisx.wrisxdapp.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Getter
@Entity
@Table(name = "facilitator")
public class Facilitator {
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
    private String description;

    @NotNull
    @Setter
    private boolean confirmed;

    public Facilitator() {
        //
    }

    public Facilitator(String address, String name, String emailAddress, String description) {
        this.address = address;
        this.name = name;
        this.emailAddress = emailAddress;
        this.description = description;
        this.confirmed = false;
    }
}
