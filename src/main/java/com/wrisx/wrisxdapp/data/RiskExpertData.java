package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.RiskExpert;

import java.io.Serializable;

public class RiskExpertData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String address;

    @JsonProperty
    private String name;

    public RiskExpertData(RiskExpert riskExpert) {
        this.id = riskExpert.getId();
        this.address = riskExpert.getAddress();
        this.name = riskExpert.getName();
    }
}
