package com.wrisx.wrisxdapp.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Expert;

import java.io.Serializable;

public class ExpertData implements Serializable {
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
    private String keywords;

    @JsonProperty
    private String description;

    public ExpertData(Expert expert) {
        this.id = expert.getId();
        this.address = expert.getAddress();
        this.name = expert.getUser().getName();
        this.emailAddress = expert.getUser().getEmailAddress();
        this.profileLink = expert.getUser().getProfileLink();
        this.websiteLink = expert.getUser().getWebsiteLink();
        this.keywords = expert.getKeywords();
        this.description = expert.getDescription();
    }
}
