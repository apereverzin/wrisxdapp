package com.wrisx.wrisxdapp.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.User;

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

    public UserData(User client) {
        this.id = client.getId();
        this.address = client.getAddress();
        this.name = client.getName();
        this.emailAddress = client.getEmailAddress();
    }
}
