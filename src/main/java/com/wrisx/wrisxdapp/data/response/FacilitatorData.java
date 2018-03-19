package com.wrisx.wrisxdapp.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.Facilitator;

import java.io.Serializable;

public class FacilitatorData implements Serializable {
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

    public FacilitatorData(Facilitator facilitator) {
        this.id = facilitator.getId();
        this.address = facilitator.getAddress();
        this.name = facilitator.getUser().getName();
        this.emailAddress = facilitator.getUser().getEmailAddress();
        this.profileLink = facilitator.getUser().getProfileLink();
        this.websiteLink = facilitator.getUser().getWebsiteLink();
        this.keywords = facilitator.getKeywords();
        this.description = facilitator.getDescription();
    }
}
