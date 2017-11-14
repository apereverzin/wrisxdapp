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
@Table(name = "risk_expert")
public class RiskExpert {
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
    private String comment;

    public RiskExpert() {
        //
    }

    public RiskExpert(String address, String name, String emailAddress, String comment) {
        this.address = address;
        this.name = name;
        this.emailAddress = emailAddress;
        this.comment = comment;
    }
}
