package com.wrisx.wrisxdapp.data.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wrisx.wrisxdapp.domain.ResearchEnquiry;

import java.io.Serializable;
import java.util.Date;

public class ResearchEnquiryData implements Serializable {
    @JsonProperty
    private long id;

    @JsonProperty
    private ClientData client;

    @JsonProperty
    private String keywords;

    @JsonProperty
    private String description;

    @JsonProperty
    private Date timestamp;

    @JsonProperty
    private EnquiryBidData enquiryBid;

    public ResearchEnquiryData(ResearchEnquiry researchEnquiry) {
        this(researchEnquiry, null);
    }

    public ResearchEnquiryData(ResearchEnquiry researchEnquiry,
                               EnquiryBidData enquiryBid) {
        this.id = researchEnquiry.getId();
        if (researchEnquiry.getClient() != null) {
            this.client = new ClientData(researchEnquiry.getClient());
        }
        this.keywords = researchEnquiry.getKeywords();
        this.description = researchEnquiry.getDescription();
        this.timestamp = researchEnquiry.getTimestamp();
        this.enquiryBid = enquiryBid;
    }
}
