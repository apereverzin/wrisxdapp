package com.wrisx.wrisxdapp.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Client;

import java.io.Serializable;

public class ClientData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private String address;

    @JsonProperty
    private String name;

    @JsonProperty
    private String emailAddress;

    @JsonProperty
    private String profileLink;

    @JsonProperty
    private String websiteLink;

    @JsonProperty
    private String description;

    public ClientData(Client client) {
        this.id = client.getId();
        this.address = client.getAddress();
        this.name = client.getUser().getName();
        this.emailAddress = client.getUser().getEmailAddress();
        this.profileLink = client.getUser().getProfileLink();
        this.websiteLink = client.getUser().getWebsiteLink();
        this.description = client.getDescription();
    }
}
