package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.RiskExpert;

import java.io.Serializable;

public class UserData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String address;

    @JsonProperty
    private String name;

    @JsonProperty
    private String emailAddress;

    @JsonProperty
    private String comment;

    public UserData(Client client) {
        this.id = client.getId();
        this.address = client.getAddress();
        this.name = client.getName();
        this.emailAddress = client.getEmailAddress();
        this.comment = client.getComment();
    }

    public UserData(RiskExpert riskExpert) {
        this.id = riskExpert.getId();
        this.address = riskExpert.getAddress();
        this.name = riskExpert.getName();
        this.emailAddress = riskExpert.getEmailAddress();
        this.comment = riskExpert.getComment();
    }
}
