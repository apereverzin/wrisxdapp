package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Client;
import com.wrisx.wrisxdapp.domain.Expert;

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
    private String description;

    public UserData(Client client) {
        this.id = client.getId();
        this.address = client.getAddress();
        this.name = client.getName();
        this.emailAddress = client.getEmailAddress();
        this.description = client.getDescription();
    }

    public UserData(Expert expert) {
        this.id = expert.getId();
        this.address = expert.getAddress();
        this.name = expert.getName();
        this.emailAddress = expert.getEmailAddress();
        this.description = expert.getDescription();
    }
}
