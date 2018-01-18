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
@Table(name = "research_binary")
public class ResearchBinary {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String uuid;

    @NotNull
    private String binary;

    public ResearchBinary() {
        //
    }

    public ResearchBinary(String uuid, String binary) {
        this.uuid = uuid;
        this.binary = binary;
    }
}
